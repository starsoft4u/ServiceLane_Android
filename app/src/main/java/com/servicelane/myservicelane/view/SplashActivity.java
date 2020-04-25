package com.servicelane.myservicelane.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.view.auth.SignUpActivity;
import com.servicelane.myservicelane.view.startup.ProfileActivity;
import com.servicelane.myservicelane.view.tutorial.TutorialActivity;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Hide status bar
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        } else {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        }
        super.onCreate(savedInstanceState);

        int count = Helper.StoreKey.LAUNCH_COUNT.getInt(0);
        if (count == 0) {
            TutorialActivity.startActivity(this);
        } else if (Helper.StoreKey.ME.getUser() == null) {
            SignUpActivity.startActivity(this);
        } else if (TextUtils.isEmpty(Helper.StoreKey.ME.getUser().getPlanName())) {
            ProfileActivity.startActivity(this);
        } else {
            MainActivity.startActivity(this);
        }

        Helper.StoreKey.LAUNCH_COUNT.setInt(count + 1);
    }
}
