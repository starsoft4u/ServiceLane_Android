package com.servicelane.myservicelane.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Alert extends Dialog {
    @BindView(R.id.textMessage)
    TextView textMessage;

    @BindView(R.id.btnNegative)
    Button btnNegative;

    @BindView(R.id.btnPositive)
    Button btnPositive;

    private String message = "";
    private String negativeTitle, positiveTitle;
    private AlertDelegate negativeAction, positiveAction;

    public Alert(@NonNull Context context) {
        super(context);
        positiveTitle = Helper.string(context, R.string.okay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog);

        ButterKnife.bind(this);
    }

    public Alert setMessage(int resId) {
        this.message = Helper.string(getContext(), resId);
        return this;
    }

    public Alert setMessage(String message) {
        this.message = message;
        return this;
    }

    public Alert setNegative(String title, AlertDelegate onNegative) {
        negativeTitle = title;
        negativeAction = onNegative;
        return this;
    }

    public Alert setNegative(int resId, AlertDelegate onNegative) {
        negativeTitle = Helper.string(getContext(), resId);
        negativeAction = onNegative;
        return this;
    }

    public Alert setPositive(String title, AlertDelegate onNegative) {
        positiveTitle = title;
        positiveAction = onNegative;
        return this;
    }

    public Alert setPositive(int resId, AlertDelegate onNegative) {
        positiveTitle = Helper.string(getContext(), resId);
        positiveAction = onNegative;
        return this;
    }

    @Override
    public void show() {
        super.show();
        textMessage.setText(message);
        if (TextUtils.isEmpty(negativeTitle)) {
            btnNegative.setVisibility(View.GONE);
        } else {
            btnNegative.setText(negativeTitle);
            btnNegative.setVisibility(View.VISIBLE);
        }
        btnPositive.setText(positiveTitle);
    }

    @OnClick(R.id.btnNegative)
    void onNegativeClicked() {
        dismiss();
        if (negativeAction != null) {
            negativeAction.onButtonClicked();
        }
    }

    @OnClick(R.id.btnPositive)
    void onPositiveClicked() {
        dismiss();
        if (positiveAction != null) {
            positiveAction.onButtonClicked();
        }
    }

    public interface AlertDelegate {
        void onButtonClicked();
    }
}
