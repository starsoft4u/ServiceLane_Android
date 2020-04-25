package com.servicelane.myservicelane.view.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.view.BaseActivity;

public class AboutUsActivity extends BaseActivity {
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AboutUsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        title(R.string.menu_about_us);
    }
}
