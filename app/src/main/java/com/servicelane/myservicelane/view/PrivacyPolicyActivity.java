package com.servicelane.myservicelane.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.servicelane.myservicelane.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyPolicyActivity extends BaseActivity {
    private static final String ARG_IS_PRIVACY = "is_privacy";

    @BindView(R.id.webView)
    WebView webView;

    public static void startActivity(Context context, boolean isPrivacy) {
        Intent intent = new Intent(context, PrivacyPolicyActivity.class);
        intent.putExtra(ARG_IS_PRIVACY, isPrivacy);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        boolean isPrivacy = getIntent().getBooleanExtra(ARG_IS_PRIVACY, true);

        title(isPrivacy ? R.string.menu_privacy_policy : R.string.terms_of_conditions);

        ButterKnife.bind(this);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(isPrivacy ? PRIVACY_POLICY_URL : TERMS_URL);
    }
}
