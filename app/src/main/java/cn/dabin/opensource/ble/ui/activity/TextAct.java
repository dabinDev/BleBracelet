package cn.dabin.opensource.ble.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseActivity;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.activity
 * Created by :  dabin.
 * Created time: 2019/9/11 0:17
 * Changed by :  dabin.
 * Changed time: 2019/9/11 0:17
 * Class description:
 */
public class TextAct extends BaseActivity {
    private String title;
    private String content;
    private TextView tvBack;
    private TextView tvTitle;
    private TextView tvContent;

    public static void openAct(Context context, String title, String content) {
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.setClass(context, TextAct.class);
        context.startActivity(intent);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_text);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorWhite)
                .navigationBarEnable(false)
                .statusBarDarkFont(true)
                .init();
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        initView();
    }

    private void initView() {
        tvBack = findViewById(R.id.tv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvTitle.setText(title);
        tvContent.setText(content);
        tvBack.setOnClickListener(v ->
        {
            this.finish();
        });
    }
}
