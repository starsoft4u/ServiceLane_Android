package com.servicelane.myservicelane.view.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.Plan;
import com.servicelane.myservicelane.view.BaseActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlanActivity extends BaseActivity {
    @BindView(R.id.textCurrentPlan)
    TextView textCurrentPlan;

    @BindView(R.id.textStatedOn)
    TextView textStatedOn;

    @BindView(R.id.textExpiresOn)
    TextView textExpiresOn;

    private Plan currentPlan;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, PlanActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        ButterKnife.bind(this);

        title(R.string.menu_plan);

        loadPlans();
    }

    private void loadPlans() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", Objects.requireNonNull(Helper.StoreKey.ME.getUser()).getIdString());

        post("/user/viewPlan", params, res -> {
            JsonObject data = res.get("data").getAsJsonObject();

            if (data.has("curPlan") && !data.get("curPlan").isJsonNull()) {
                currentPlan = new Plan(data.get("curPlan").getAsJsonArray().get(0).getAsJsonObject());
                textCurrentPlan.setText(currentPlan.title());
                textStatedOn.setText(currentPlan.getStartOn());
                textExpiresOn.setText(currentPlan.getExpireOn());
            }
        });
    }
}
