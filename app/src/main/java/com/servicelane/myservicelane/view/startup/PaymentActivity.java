package com.servicelane.myservicelane.view.startup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.User;
import com.servicelane.myservicelane.view.Alert;
import com.servicelane.myservicelane.view.BaseActivity;
import com.servicelane.myservicelane.view.MainActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ButterKnife.bind(this);

        title(R.string.menu_payment);
    }

    @OnClick(R.id.btnProceed)
    public void onProceedToPlans() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAN_URL + Objects.requireNonNull(Helper.StoreKey.ME.getUser()).getId())));
    }

    @OnClick(R.id.btnDone)
    public void onDonePayment() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", Objects.requireNonNull(Helper.StoreKey.ME.getUser()).getIdString());

        post("/user/profile", params, response -> {
            JsonObject data = response.getAsJsonObject("data");
            User user = new User(data.getAsJsonObject("userInfo"));
            if (TextUtils.isEmpty(user.getPlanName())) {
                toast(R.string.msg_startup_payment_fail);
            } else {
                Helper.StoreKey.ME.setUser(user);
                new Alert(this)
                        .setMessage(R.string.msg_startup_payment)
                        .setPositive(R.string.close, () -> MainActivity.startActivity(PaymentActivity.this))
                        .show();
            }
        });
    }
}
