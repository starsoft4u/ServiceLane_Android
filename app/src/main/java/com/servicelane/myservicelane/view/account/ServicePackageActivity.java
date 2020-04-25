package com.servicelane.myservicelane.view.account;

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
import com.servicelane.myservicelane.model.ServicePackage;
import com.servicelane.myservicelane.view.Alert;
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

public class ServicePackageActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<ServicePackage> services = new ArrayList<>();
    private ServiceAdapter adapter;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ServicePackageActivity.class));
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

            post("/pvService/addService", params, response -> new Alert(this)
                    .setMessage(R.string.service_package_add_success)
                    .setPositive(R.string.okay, this::loadServicePackage)
                    .show());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycler_view_only);

        ButterKnife.bind(this);

        title(R.string.menu_service_packages);

        adapter = new ServiceAdapter(this, services, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        loadServicePackage();

        // Event
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
