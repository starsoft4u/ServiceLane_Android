package com.servicelane.myservicelane.view.startup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.annimon.stream.Stream;
import com.google.gson.JsonArray;
import com.servicelane.myservicelane.Events;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.adapter.ServiceAdapter;
import com.servicelane.myservicelane.model.Portfolio;
import com.servicelane.myservicelane.model.ServicePackage;
import com.servicelane.myservicelane.view.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServicePackageActivity extends BaseActivity {
    private static final String ARG_SERVICES = "services";
    private static final String ARG_PORTFOLIOS = "portfolio";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<ServicePackage> services = new ArrayList<>();
    private List<Portfolio> portfolios = new ArrayList<>();
    private ServiceAdapter adapter;

    public static void startActivity(Context context, List<ServicePackage> services, List<Portfolio> portfolios) {
        Intent intent = new Intent(context, ServicePackageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putParcelableArrayListExtra(ARG_SERVICES, (ArrayList<ServicePackage>) services);
        intent.putParcelableArrayListExtra(ARG_PORTFOLIOS, (ArrayList<Portfolio>) portfolios);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_package);

        ButterKnife.bind(this);

        title(R.string.menu_service_packages);

        Intent intent = getIntent();
        if (intent != null) {
            services = intent.getParcelableArrayListExtra(ARG_SERVICES);
            portfolios = intent.getParcelableArrayListExtra(ARG_PORTFOLIOS);
        }

        adapter = new ServiceAdapter(this, services, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        // Event
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRemoveAction(Events.OnRemoveServicePackage event) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", Objects.requireNonNull(Helper.StoreKey.ME.getUser()).getIdString());
        params.put("serviceId", String.valueOf(event.serviceId));

        post("/pvService/removeService", params, response -> loadServicePackage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveAction(Events.OnAddServicePackage event) {
        if (TextUtils.isEmpty(event.name)) {
            toast(R.string.msg_service_name);
        } else if (TextUtils.isEmpty(event.description)) {
            toast(R.string.msg_service_description);
        } else if (!TextUtils.isDigitsOnly(event.rate)) {
            toast(R.string.msg_service_rate);
        } else if (event.duration == ServicePackage.Duration.NONE) {
            toast(R.string.msg_service_duration);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("userId", Objects.requireNonNull(Helper.StoreKey.ME.getUser()).getIdString());
            params.put("srvName", event.name);
            params.put("srvDesc", event.description);
            params.put("rate", event.rate);
            params.put("per", event.duration.unit());

            post("/pvService/addService", params, response -> loadServicePackage());
        }
    }

    @OnClick(R.id.btnProceed)
    void onProceedToPhotos() {
        if (services.isEmpty()) {
            toast(R.string.msg_empty_service_packages);
        } else {
            PortfolioActivity.startActivity(this, portfolios);
        }
    }

    private void loadServicePackage() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", Objects.requireNonNull(Helper.StoreKey.ME.getUser()).getIdString());

        post("/user/profile", params, response -> {
            services.clear();
            JsonArray items = response.get("data").getAsJsonObject().get("pvList").getAsJsonArray();
            services.addAll(Stream.of(items).map(item -> new ServicePackage(item.getAsJsonObject())).toList());
            adapter.notifyDataSetChanged();
        });
    }
}
