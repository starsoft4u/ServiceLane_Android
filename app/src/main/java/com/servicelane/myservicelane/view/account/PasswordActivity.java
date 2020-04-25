package com.servicelane.myservicelane.view.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.view.BaseActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordActivity extends BaseActivity {
    @BindView(R.id.editOldPassword)
    EditText oldPwd;

    @BindView(R.id.editNewPassword)
    EditText newPwd;

    @BindView(R.id.editConfirmPassword)
    EditText confirmPwd;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, PasswordActivity.class));
    }

    @OnClick(R.id.btnSave)
    void onUpdatePassword() {
        if (TextUtils.isEmpty(newPwd.getText())) {
            toast(R.string.msg_pwd_new_pwd);
        } else if (!TextUtils.equals(newPwd.getText(), confirmPwd.getText())) {
            toast(R.string.msg_pwd_confirm_pwd);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("userEmail", Helper.StoreKey.ME.getUser().getEmail());
            params.put("curPwd", oldPwd.getText().toString());
            params.put("newPwd", newPwd.getText().toString());

            post("/user/updatePwd", params, response -> {
                oldPwd.setText("");
                newPwd.setText("");
                confirmPwd.setText("");
                toast(R.string.msg_pwd_success);
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        ButterKnife.bind(this);

        title(R.string.menu_password);
    }
}
