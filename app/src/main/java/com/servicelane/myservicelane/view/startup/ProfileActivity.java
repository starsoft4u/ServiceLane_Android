package com.servicelane.myservicelane.view.startup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.PatternsCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.Common;
import com.servicelane.myservicelane.model.Portfolio;
import com.servicelane.myservicelane.model.ServicePackage;
import com.servicelane.myservicelane.model.User;
import com.servicelane.myservicelane.view.BaseActivity;
import com.squareup.picasso.Picasso;

import net.alhazmy13.gota.Gota;
import net.alhazmy13.gota.GotaResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.apptik.widget.multiselectspinner.MultiSelectSpinner;
import pl.aprilapps.easyphotopicker.EasyImage;

public class ProfileActivity extends BaseActivity implements Gota.OnRequestPermissionsBack {
    @BindView(R.id.imgAvatar)
    CircleImageView imgAvatar;

    @BindView(R.id.editFirstName)
    EditText editFirstName;

    @BindView(R.id.editLastName)
    EditText editLastName;

    @BindView(R.id.editEmail)
    EditText editEmail;

    @BindView(R.id.editPhone)
    EditText editPhone;

    @BindView(R.id.editWhatsApp)
    EditText editWhatsApp;

    @BindView(R.id.editAddress)
    EditText editAddress;

    @BindView(R.id.editParish)
    AppCompatSpinner pickerParish;

    @BindView(R.id.editCategory)
    MultiSelectSpinner pickerCategory;

    @BindView(R.id.editFacebook)
    EditText editFacebook;

    @BindView(R.id.editWebsite)
    EditText editWebsite;

    @BindView(R.id.editShortBio)
    EditText editShortBio;

    @BindView(R.id.textWordsCnt)
    TextView textWordCnt;

    private List<Portfolio> portfolios;
    private List<ServicePackage> services;
    private List<String> parishes = new ArrayList<>();
    private List<Common> categories = new ArrayList<>();
    private File image;
    private InputFilter filter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_1);

        ButterKnife.bind(this);

        title(R.string.menu_edit_profile);

        // Allow scroll on short bio multiline edit text
        editShortBio.setMovementMethod(new ScrollingMovementMethod());
        editShortBio.setOnTouchListener((view, motionEvent) -> {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return false;
        });
        editShortBio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int count = wordCount(charSequence.toString());
                if (count >= 140) {
                    filter = new InputFilter.LengthFilter(editShortBio.getText().length());
                    editShortBio.setFilters(new InputFilter[]{filter});
                } else if (filter != null) {
                    editShortBio.setFilters(new InputFilter[0]);
                    filter = null;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textWordCnt.setText(Helper.string(ProfileActivity.this, R.string.with_words, 140 - wordCount(editShortBio.getText().toString())));
            }
        });

        // App links.
        Intent appLinkIntent = getIntent();
        Uri appLinkData = appLinkIntent.getData();

        loadProfile(appLinkData);
    }

    private void loadProfile(Uri uri) {
        Map<String, String> params = new HashMap<>();
        if (uri == null) {
            params.put("userId", Objects.requireNonNull(Helper.StoreKey.ME.getUser()).getIdString());
        } else {
            String key = uri.getQueryParameter("key");
            byte[] decoded = Base64.decode(key, Base64.DEFAULT);
            String email = new String(decoded);
            params.put("email", email);
        }

        post("/user/profile", params, response -> {
            toast(uri == null ? R.string.msg_startup_profile_alert : R.string.msg_startup_profile_email);

            JsonObject data = response.getAsJsonObject("data");
            User user = new User(data.getAsJsonObject("userInfo"));
            Helper.StoreKey.ME.setUser(user);

            Picasso.get().load(Objects.requireNonNull(user).getPhoto()).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(imgAvatar);
            editFirstName.setText(user.getFirstName());
            editLastName.setText(user.getLastName());
            editEmail.setText(user.getEmail());
            editPhone.setText(user.getPhone());
            editWhatsApp.setText(user.getWhatsApp());
            editAddress.setText(user.getAddress());
            editFacebook.setText(user.getFacebook());
            editWebsite.setText(user.getWebSite());
            textWordCnt.setText(Helper.string(this, R.string.with_words, 140 - wordCount(user.getShortBio())));
            editShortBio.setText(user.getShortBio());

            parishes = Stream.of(data.getAsJsonArray("parishes")).map(JsonElement::getAsString).toList();
            parishes.add(0, "Select parish");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.picker_text, parishes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            pickerParish.setAdapter(adapter);
            int position = parishes.indexOf(Objects.requireNonNull(user).getParish());
            if (position > 0) {
                pickerParish.setSelection(position);
            }

            categories = Stream.of(data.getAsJsonArray("catList")).map(item -> {
                JsonObject json = item.getAsJsonObject();
                return new Common(json.get("ID").getAsInt(), json.get("Category").getAsString());
            }).toList();
            ArrayAdapter<Common> adapter1 = new ArrayAdapter<>(this, R.layout.layout_check_text, categories);
            pickerCategory.setListAdapter(adapter1);
            pickerCategory.setMaxSelectedItems(2);
            for (int i = 0; i < categories.size(); i++) {
                Common cat = categories.get(i);
                boolean checked = Stream.of(user.getCategoryList()).filter(item -> item.getId() == cat.getId()).count() > 0;
                pickerCategory.selectItem(i, checked);
            }

            portfolios = Stream.of(data.get("prList").getAsJsonArray()).map(item -> new Portfolio(item.getAsJsonObject())).toList();
            services = Stream.of(data.get("pvList").getAsJsonArray()).map(item -> new ServicePackage(item.getAsJsonObject())).toList();
        });
    }

    @OnClick(R.id.btnUploadPhoto)
    public void onUploadPhotoAction() {
        new Gota.Builder(this)
                .withPermissions(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .requestId(0)
                .setListener(this)
                .check();
    }

    @OnClick(R.id.btnProceed)
    public void onSaveAction() {
        int[] ids = IntStream.range(0, categories.size()).filter(i -> pickerCategory.getSelected()[i]).map(i -> categories.get(i).getId()).toArray();
        if (TextUtils.isEmpty(editFirstName.getText())) {
            toast(R.string.msg_first_name);
        } else if (TextUtils.isEmpty(editLastName.getText())) {
            toast(R.string.msg_last_name);
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(editEmail.getText()).matches()) {
            toast(R.string.msg_email);
        } else if (pickerParish.getSelectedItemPosition() == 0) {
            toast(R.string.msg_parish_error);
        } else if (ids.length == 0) {
            toast(R.string.msg_category_error);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("userId", Objects.requireNonNull(Helper.StoreKey.ME.getUser()).getIdString());
            params.put("fname", editFirstName.getText().toString());
            params.put("lname", editLastName.getText().toString());
            params.put("email", editEmail.getText().toString());
            params.put("phone", editPhone.getText().toString());
            params.put("whatsapp", editWhatsApp.getText().toString());
            params.put("address", editAddress.getText().toString());
            params.put("parish", pickerParish.getSelectedItem().toString());
            params.put("fb_link", editFacebook.getText().toString());
            params.put("web_link", editWebsite.getText().toString());
            params.put("short_bio", editShortBio.getText().toString());
            params.put("categoryIds", Arrays.toString(ids).replace("[", "").replace("]", ""));

            if (image == null) {
                post("/user/updateinfo", params, response -> {
                    dismissKeyboard();
                    Helper.StoreKey.ME.setUser(new User(response.get("data").getAsJsonObject()));
                    ServicePackageActivity.startActivity(this, services, portfolios);
                });
            } else {
                Map<String, File> files = new HashMap<>();
                files.put("photo", image);

                multipart("/user/updateinfo", files, params, response -> {
                    dismissKeyboard();
                    Helper.StoreKey.ME.setUser(new User(response.get("data").getAsJsonObject()));
                    ServicePackageActivity.startActivity(this, services, portfolios);
                });
            }
        }
    }

    private int wordCount(String str) {
        String trim = str.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length;
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
                    image = imageFiles.get(0);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inSampleSize = 1;
                    Bitmap bitmap = BitmapFactory.decodeFile(imageFiles.get(0).getAbsolutePath(), options);
                    imgAvatar.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                Log.d(TAG, "Cancel to pick image.");
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(ProfileActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }
}