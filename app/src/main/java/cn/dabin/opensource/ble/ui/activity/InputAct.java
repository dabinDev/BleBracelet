package cn.dabin.opensource.ble.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseActivity;
import cn.dabin.opensource.ble.network.BleApi;
import cn.dabin.opensource.ble.network.bean.ModelBean;
import cn.dabin.opensource.ble.network.callback.MineStringCallback;
import cn.dabin.opensource.ble.util.StringUtils;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.activity
 * Created by :  dabin.
 * Created time: 2019/9/8 1:14
 * Changed by :  dabin.
 * Changed time: 2019/9/8 1:14
 * Class description:
 */
public class InputAct extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rlTop;
    private TextView tvBack;
    private TextView tvTitle;
    private View infoLine;
    private EditText etInput;
    private Button btnDoSet;
    private String title;
    private String value;

    public static void openAct(Context context, String title, String value) {
        Intent intent = new Intent();
        intent.setClass(context, InputAct.class);
        intent.putExtra("title", title);
        intent.putExtra("value", value);
        context.startActivity(intent);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_input);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorWhite)
                .navigationBarEnable(false)
                .statusBarDarkFont(true)
                .init();
        initView();
    }

    private void initView() {
        rlTop = findViewById(R.id.rl_top);
        tvBack = findViewById(R.id.tv_back);
        tvTitle = findViewById(R.id.tv_title);
        infoLine = findViewById(R.id.info_line);
        etInput = findViewById(R.id.et_input);
        btnDoSet = findViewById(R.id.btn_do_set);
        btnDoSet.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        title = getIntent().getStringExtra("title");
        value = getIntent().getStringExtra("value");
        if (StringUtils.isEmpty(title)) {
            tvTitle.setText("标题");
        }

        tvTitle.setText(title);
        if (title.contains("昵称")) {
            etInput.setHint("请输入昵称");
        }

        if (StringUtils.isNotEmpty(value)) {
            etInput.setText(value);
        }

    }

    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.iv_head:
                //todo 选择头像
                break;
            case R.id.btn_do_set:
                String nickName = etInput.getText().toString();
                if (StringUtils.isNotEmpty(nickName)) {
                    HttpParams params1 = new HttpParams();
                    params1.put("nickName", nickName);
                    HttpHeaders header = new HttpHeaders("token", readToken());
                    OkGo.<String>get(BleApi.getUrl(BleApi.userEdit)).headers(header).params(params1).tag(this).execute(new MineStringCallback() {
                        @Override public void success(String result) {
                            ModelBean bean = new Gson().fromJson(result, ModelBean.class);
                            if (bean.getSuccess()) {
                                //showCenterSuccessMsg("登录成功");
                                Intent intent = new Intent();
                                intent.putExtra("nickName", nickName);
                                setResult(RESULT_OK, intent);
                            } else {
                                showCenterInfoMsg(bean.getMessage());
                            }
                        }
                    });
                } else {
                    finish();
                }

                break;
            default:
                break;

        }
    }
}
