package com.servicelane.myservicelane.view.auth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.PatternsCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.User;
import com.servicelane.myservicelane.view.Alert;
import com.servicelane.myservicelane.view.BaseActivity;
import com.servicelane.myservicelane.view.MainActivity;
import com.servicelane.myservicelane.view.PrivacyPolicyActivity;
import com.servicelane.myservicelane.view.startup.ProfileActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity {
    @BindView(R.id.editFirstName)
    EditText editFirstName;

    @BindView(R.id.editLastName)
    EditText editLastName;

    @BindView(R.id.editEmail)
    EditText editEmail;

    @BindView(R.id.editPassword)
    EditText editPassword;

    @BindView(R.id.textAgree)
    TextView textAgree;

    private CallbackManager callbackManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        // Terms of service
        String text = Helper.string(this, R.string.by_signing_up);
        int start, end;
        Spannable span = new SpannableString(text);
        start = text.indexOf("terms of service");
        end = start + "terms of service".length();
        span.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                PrivacyPolicyActivity.startActivity(SignUpActivity.this, false);
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Privacy policy
        start = text.indexOf("privacy policy");
        end = start + "privacy policy".length();
        span.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                PrivacyPolicyActivity.startActivity(SignUpActivity.this, true);
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textAgree.setText(span, TextView.BufferType.SPANNABLE);
        textAgree.setMovementMethod(LinkMovementMethod.getInstance());
        textAgree.setHighlightColor(Color.TRANSPARENT);

        // Facebook
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                toast(R.string.facebook_login_failed);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btnProceed)
    void onProceedAction() {
        MainActivity.startActivity(this);
    }

    @OnClick(R.id.btnLogin)
    void onLoginAction() {
        LoginActivity.startActivity(this);
    }

    @OnClick(R.id.btnFacebook)
    void onFacebookAction() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            handleFacebookAccessToken(accessToken);
        } else {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        }
    }

    @OnClick(R.id.btnSignUp)
    void onSignUpAction() {
        if (TextUtils.isEmpty(editFirstName.getText())) {
            toast(R.string.msg_first_name);
        } else if (TextUtils.isEmpty(editLastName.getText())) {
            toast(R.string.msg_last_name);
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(editEmail.getText()).matches()) {
            toast(R.string.msg_email);
        } else if (TextUtils.isEmpty(editPassword.getText())) {
            toast(R.string.msg_password);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("firstName", editFirstName.getText().toString());
            params.put("lastName", editLastName.getText().toString());
            params.put("email", editEmail.getText().toString());
            params.put("password", editPassword.getText().toString());

            post("/user/signup", params, response -> {
                Helper.StoreKey.ME.setUser(new User(response.get("data").getAsJsonObject()));
                new Alert(this)
                        .setMessage(R.string.msg_sign_up_confirm)
                        .show();
            });
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token, (object, response) -> {
            Log.w(TAG, "Graph request success: " + response);
            try {
                String id = object.getString("id");
                String email = object.getString("email");
                String firstName = object.getString("first_name");
                String lastName = object.getString("last_name");
                requestFacebookAvatar(token, id, email, firstName, lastName);
            } catch (Exception e) {
                Log.d(TAG, "Facebook account retrieve failed", e);
                toast(R.string.facebook_login_failed);
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void requestFacebookAvatar(AccessToken token, String id, String email, String firstName, String lastName) {
        GraphRequest request = new GraphRequest(token, "/" + id + "/picture", null, HttpMethod.GET, response -> {
            try {
                String avatar = response.getJSONObject().getJSONObject("data").getString("url");
                socialSignUp(email, firstName, lastName, avatar);
            } catch (Exception e) {
                socialSignUp(email, firstName, lastName, "");
            }
        });
        Bundle parameters = new Bundle();
        parameters.putBoolean("redirect", false);
        parameters.putString("type", "normal");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void socialSignUp(String email, String firstName, String lastName, String avatar) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("avatar", avatar);

        post("/user/socialSignUp", params, response -> {
            Helper.StoreKey.ME.setUser(new User(response.get("data").getAsJsonObject()));
            ProfileActivity.startActivity(this);
        });
    }
}
