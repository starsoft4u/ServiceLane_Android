package com.servicelane.myservicelane.view.setting;

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

public class SupportActivity extends BaseActivity {
    @BindView(R.id.editMessage)
    EditText editMessage;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SupportActivity.class));
    }

    @OnClick(R.id.btnSend)
    void onSendMessageAction() {
        if (Helper.StoreKey.ME.getUser() == null) {
            toast(R.string.msg_login_to_support);
        } else if (TextUtils.isEmpty(editMessage.getText())) {
            toast(R.string.msg_support_error);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("userId", Helper.StoreKey.ME.getUser().getIdString());
            params.put("content", editMessage.getText().toString());

            post("/user/sendEmail", params, response -> {
                dismissKeyboard();
                editMessage.setText("");
                toast(R.string.msg_sent_success);
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        ButterKnife.bind(this);

        title(R.string.menu_support);
    }
}
