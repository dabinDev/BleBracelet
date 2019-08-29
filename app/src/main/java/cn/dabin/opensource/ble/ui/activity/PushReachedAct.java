package cn.dabin.opensource.ble.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.dabin.opensource.ble.R;
import cn.jpush.android.api.JPushInterface;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.activity
 * Created by :  dabin.
 * Created time: 2019/8/29 23:28
 * Changed by :  dabin.
 * Changed time: 2019/8/29 23:28
 * Class description:
 */
public class PushReachedAct extends AppCompatActivity {
    private TextView tvPushMsg;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_push_reach);
        initView();
    }

    private void initView() {
        tvPushMsg = findViewById(R.id.tvPushMsg);
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String title = null;
            String content = null;
            if (bundle != null) {
                title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                content = bundle.getString(JPushInterface.EXTRA_ALERT);
            }
            tvPushMsg.setText("Title : " + title + "  " + "Content : " + content);
        }
    }
}
