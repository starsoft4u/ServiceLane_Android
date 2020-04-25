package com.servicelane.myservicelane.view.startup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.annimon.stream.Stream;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.JsonObject;
import com.servicelane.myservicelane.Events;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.adapter.GridDecoration;
import com.servicelane.myservicelane.adapter.PortfolioAdapter;
import com.servicelane.myservicelane.model.Portfolio;
import com.servicelane.myservicelane.view.Alert;
import com.servicelane.myservicelane.view.BaseActivity;
import com.stfalcon.frescoimageviewer.ImageViewer;

import net.alhazmy13.gota.Gota;
import net.alhazmy13.gota.GotaResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.EasyImage;

public class PortfolioActivity extends BaseActivity implements Gota.OnRequestPermissionsBack {
    private static final String ARG_PORTFOLIOS = "portfolio";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<Portfolio> portfolios = new ArrayList<>();
    private PortfolioAdapter adapter;

    public static void startActivity(Context context, List<Portfolio> portfolios) {
        Intent intent = new Intent(context, PortfolioActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putParcelableArrayListExtra(ARG_PORTFOLIOS, (ArrayList<Portfolio>) portfolios);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);

        setContentView(R.layout.activity_portfolio);

        ButterKnife.bind(this);

        title(R.string.menu_portfolio);

        Intent intent = getIntent();
        if (intent != null) {
            portfolios = intent.getParcelableArrayListExtra(ARG_PORTFOLIOS);
        }

        adapter = new PortfolioAdapter(this, Helper.StoreKey.ME.getUser(), portfolios, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return i == 0 ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridDecoration(2, Helper.pixel(this, 16), true, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.btnProceed)
    void onProceedToPayment() {
        PaymentActivity.startActivity(this);
    }

    private void loadPortfolio() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", Objects.requireNonNull(Helper.StoreKey.ME.getUser()).getIdString());

        post("/user/profile", params, response -> {
            JsonObject data = response.get("data").getAsJsonObject();
            portfolios.clear();
            portfolios.addAll(Stream.of(data.get("prList").getAsJsonArray()).map(item -> new Portfolio(item.getAsJsonObject())).toList());
            adapter.notifyDataSetChanged();
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoTapped(Events.OnPortfolioPhotoTapped event) {
        int position = portfolios.indexOf(event.portfolio);
        new ImageViewer.Builder<>(this, portfolios)
                .setFormatter(Portfolio::getImage)
                .setStartPosition(position)
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeletePortfolio(Events.OnDeletePortfolio event) {
        new Alert(this)
                .setMessage(R.string.delete_portfolio)
                .setNegative(R.string.cancel, null)
                .setPositive(R.string.delete, () -> {
                    Map<String, String> params = new HashMap<>();
                    params.put("userId", Objects.requireNonNull(Helper.StoreKey.ME.getUser()).getIdString());
                    params.put("id", String.valueOf(event.portfolio.getId()));

                    post("/user/deletePortfolio", params, response -> {
                        portfolios.remove(event.portfolio);
                        adapter.notifyDataSetChanged();
                    });
                })
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddPortfolioAction(Events.OnAddPortfolio event) {
        if (portfolios.size() < 2) {
            new Gota.Builder(this)
                    .withPermissions(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .requestId(0)
                    .setListener(this)
                    .check();
        } else {
            toast(R.string.msg_max_portfolio);
        }
    }

    @Override
    public void onRequestBack(int requestId, @NonNull GotaResponse gotaResponse) {
        if (gotaResponse.isAllGranted()) {
            EasyImage.configuration(this).setAllowMultiplePickInGallery(false);
            EasyImage.openChooserWithGallery(this, "Choose Picture", 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                if (!imageFiles.isEmpty()) {
                    File upload = imageFiles.get(0);

                    // Resize if larger than 800 * 800
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(upload.getAbsolutePath(), options);
                    if (options.outWidth > 800 || options.outHeight > 800) {
                        float scale = Math.min(800f / options.outWidth, 800f / options.outHeight);
                        Bitmap out = Bitmap.createScaledBitmap(bitmap, Math.round(scale * options.outWidth), Math.round(scale * options.outHeight), false);
                        upload = new File(upload.getParent(), "file0.jpg");
                        try {
                            FileOutputStream outputStream = new FileOutputStream(upload);
                            out.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            out.recycle();
                            bitmap.recycle();
                        } catch (Exception ignored) {
                            return;
                        }
                    }

                    Map<String, File> files = new HashMap<>();
                    files.put("photo", upload);

                    Map<String, String> params = new HashMap<>();
                    params.put("userId", Objects.requireNonNull(Helper.StoreKey.ME.getUser()).getIdString());

                    multipart("/user/addPortfolio", files, params, response -> loadPortfolio());
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                Log.d(TAG, "Cancel to pick image.");
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(PortfolioActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }
}
