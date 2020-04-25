package com.servicelane.myservicelane.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.view.auth.LoginActivity;
import com.servicelane.myservicelane.view.auth.SignUpActivity;
import com.servicelane.myservicelane.view.startup.PaymentActivity;
import com.servicelane.myservicelane.view.startup.PortfolioActivity;
import com.servicelane.myservicelane.view.startup.ProfileActivity;
import com.servicelane.myservicelane.view.startup.ServicePackageActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import okhttp3.OkHttpClient;

import static android.graphics.PorterDuff.Mode.SRC_IN;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    public static final String TAG = "$[SERVICE_LANE]";
    public static final String BASE_URL = "https://myservicelane.com";
    public static final String PRIVACY_POLICY_URL = BASE_URL + "/privacy-app.php";
    public static final String TERMS_URL = BASE_URL + "/terms-app.php";
    public static final String PLAN_URL = BASE_URL + "/plans.php?userId=";
    public static final String RESET_PWD_URL = BASE_URL + "/forgot_password.php";
    public static final String PROFILE_URL = BASE_URL + "/admin/upload/profile";
    public static final String PORTFOLIO_URL = BASE_URL + "/admin/upload/portfolio";
    public static final String API_URL = BASE_URL + "/api/index.php/api";

    View progressView;
    int onStartCnt = 0;

    @Override
    protected void onStart() {
        super.onStart();
        if (onStartCnt > 1) {
            this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        } else if (onStartCnt == 1) {
            onStartCnt++;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);

        onStartCnt = 1;
        if (savedInstanceState == null) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        } else {
            onStartCnt = 2;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && !(this instanceof SplashActivity) && !(this instanceof LoginActivity) && !(this instanceof SignUpActivity) && !(this instanceof MainActivity)
                && !(this instanceof ProfileActivity) && !(this instanceof ServicePackageActivity) && !(this instanceof PortfolioActivity) && !(this instanceof PaymentActivity)) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initialize();
    }

    @Override
    public void finish() {
        dismissKeyboard();
        super.finish();
    }

    void initialize() {
        // networking
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
        // Storage
        Helper.initialize(this);
    }

    protected void title(int res) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(res);
        }
    }

    protected void title(CharSequence title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void toast(int resId) {
//        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
        new Alert(this).setMessage(resId).show();
    }

    protected void toast(String message) {
        if (message != null) {
//            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            new Alert(this).setMessage(message).show();
        }
    }

    protected void dismissKeyboard() {
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null && imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showActivityIndicator() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (progressView == null) {
            progressView = getLayoutInflater().inflate(R.layout.progress_bar_overlay, null);

            // Progress bar tint color to white
            ProgressBar progressBar = progressView.findViewById(R.id.progress_bar);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
                DrawableCompat.setTint(wrapDrawable, Color.WHITE);
                progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
            } else {
                progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, SRC_IN);
            }

            // Add progress view
            ViewGroup viewGroup = findViewById(android.R.id.content);
            viewGroup.addView(progressView);
        }
    }

    void hideActivityIndicator() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (progressView != null) {
            ViewGroup viewGroup = findViewById(android.R.id.content);
            viewGroup.removeView(progressView);
            progressView = null;
        }
    }

    protected void post(String url, JsonObjectListener listener) {
        post(url, null, true, listener);
    }

    protected void post(String url, Map<String, String> params, JsonObjectListener listener) {
        post(url, params, true, listener);
    }

    protected void post(String url, Map<String, String> params, boolean showActivityIndicator, JsonObjectListener listener) {
        dismissKeyboard();

        if (showActivityIndicator) showActivityIndicator();

        ANRequest.PostRequestBuilder builder = AndroidNetworking.post(API_URL + url)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Accept", "application/json");

        if (params != null)
            builder.addBodyParameter(params);

        builder.build().getAsJSONObject(handleResponse(listener));
    }

    protected void get(String url, JsonObjectListener listener) {
        get(url, null, true, listener);
    }

    protected void get(String url, Map<String, String> params, JsonObjectListener listener) {
        get(url, params, true, listener);
    }

    protected void get(String url, Map<String, String> params, boolean showActivityIndicator, JsonObjectListener listener) {
        dismissKeyboard();

        if (showActivityIndicator) showActivityIndicator();

        ANRequest.GetRequestBuilder builder = AndroidNetworking.get(API_URL + url)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Accept", "application/json");

        if (params != null)
            builder.addQueryParameter(params);

        builder.build().getAsJSONObject(handleResponse(listener));
    }

    protected void multipart(String url, Map<String, File> files, Map<String, String> params, JsonObjectListener listener) {
        dismissKeyboard();
        showActivityIndicator();
        ANRequest.MultiPartBuilder builder = AndroidNetworking.upload(API_URL + url)
                .addMultipartFile(files)
                .addMultipartParameter(params)
                .addHeaders("Content-Type", "multipart/form-data")
                .addHeaders("Accept", "application/json");

        builder.build().getAsJSONObject(handleResponse(listener));
    }

    protected JSONObjectRequestListener handleResponse(JsonObjectListener listener) {
        return new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideActivityIndicator();
                JsonObject json = new JsonParser().parse(response.toString()).getAsJsonObject();
                if (json.has("status") && json.get("status").getAsBoolean()) {
                    listener.onResponse(json);
                } else {
                    toast(json.has("error") ? json.get("error").getAsString() : "Somethings went wrong, try again.");
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.d(TAG, "==== ERROR ==== code=" + anError.getErrorCode() + ", message=" + anError.getErrorDetail());
                hideActivityIndicator();
                toast("Somethings went wrong, try again.");
            }
        };
    }

    public interface JsonObjectListener {
        void onResponse(JsonObject response);
    }
}
