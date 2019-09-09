package cn.dabin.opensource.ble.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.lang.ref.WeakReference;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseActivity;
import cn.dabin.opensource.ble.network.BleApi;
import cn.dabin.opensource.ble.network.bean.LoginBean;
import cn.dabin.opensource.ble.network.bean.LoginInfo;
import cn.dabin.opensource.ble.network.bean.ModelBean;
import cn.dabin.opensource.ble.network.callback.MineStringCallback;
import cn.dabin.opensource.ble.ui.view.ImageViewWithStroke;
import cn.dabin.opensource.ble.util.FormatUtil;
import cn.dabin.opensource.ble.util.StringUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.activity
 * Created by :  dabin.
 * Created time: 2019/8/27 15:01
 * Changed by :  dabin.
 * Changed time: 2019/8/27 15:01
 * Class description:
 */
public class LoginAct extends BaseActivity implements View.OnClickListener {


    private Button btnLogin;
    private EditText etPhoneValue;
    private EditText etVercodeValue;
    private Button btnVercode;
    private ImageViewWithStroke ivHeaderPic;
    private VercodeHandler myHandler = new VercodeHandler(this);
    private String phone;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        initView();
    }

    private void initView() {
        btnLogin = findViewById(R.id.btn_login);
        etPhoneValue = findViewById(R.id.et_phone_value);
        etVercodeValue = findViewById(R.id.et_vercode_value);
        btnVercode = findViewById(R.id.btn_vercode);
        ivHeaderPic = findViewById(R.id.iv_header_pic);
        btnLogin.setOnClickListener(this);
        btnVercode.setOnClickListener(this);
        if (StringUtils.isNotEmpty(readToken())) {
            this.finish();
            HomeAct.openAct(LoginAct.this);
        }
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_vercode:
                phone = etPhoneValue.getText().toString().trim();
                if (StringUtils.isEmpty(phone)) {
                    showCenterInfoMsg("请输入手机号码");
                    return;
                }
                if (!FormatUtil.isMobileNO(phone)) {
                    showCenterInfoMsg("请输入正确的手机号码");
                    return;
                }
                HttpParams params1 = new HttpParams();
                params1.put("mobile", phone);
                OkGo.<String>get(BleApi.getUrl(BleApi.getcode)).params(params1).tag(this).execute(new MineStringCallback() {
                    @Override public void success(String result) {
                        ModelBean bean = new Gson().fromJson(result, ModelBean.class);
                        if (bean.getSuccess()) {
                            //showCenterSuccessMsg("登录成功");
                            sendMessageClick();//发送验证码
                        } else {
                            showCenterInfoMsg(bean.getMessage());
                        }
                    }
                });
                break;
            case R.id.btn_login:
                phone = etPhoneValue.getText().toString().trim();
                String code = etVercodeValue.getText().toString().trim();
                if (StringUtils.isEmpty(code)) {
                    showCenterInfoMsg("请输入验证码");
                    return;
                }
                //这是{"username":"admin","password":"admin"}的bean文件
                LoginInfo login = new LoginInfo();
                login.setCode(code);
                login.setMobile(phone);
                Gson gson = new Gson();
                String toJson = gson.toJson(login);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
                OkGo.<String>post(BleApi.getUrl(BleApi.login)).upRequestBody(requestBody).tag(this).execute(new MineStringCallback() {
                    @Override public void success(String result) {
                        LoginBean bean = new Gson().fromJson(result, LoginBean.class);
                        if (bean.getSuccess()) {
                            showCenterSuccessMsg("登录成功");
                            saveMobile(phone);
                            saveToken(bean.getModel());
                            HomeAct.openAct(LoginAct.this);
                            finish();
                        } else {
                            showCenterInfoMsg(bean.getMessage());
                        }
                    }
                });
                break;
            default:
                break;

        }
    }


    //监听按钮下直接调用即可
    private void sendMessageClick() {
        new Thread(() -> {
            for (int i = 59; i >= 0; i--) {
                Message msg = myHandler.obtainMessage();
                msg.arg1 = i;
                myHandler.sendMessage(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //region 发送验证码倒计时按钮
    private class VercodeHandler extends Handler {
        private final WeakReference<LoginAct> weakReference;

        public VercodeHandler(LoginAct activity) {
            weakReference = new WeakReference<LoginAct>(activity);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoginAct activity = weakReference.get();
            if (activity != null && msg != null) {
                if (btnVercode != null) {
                    switch (msg.what) {
                        case 0:
                            if (msg.arg1 == 0) {
                                btnVercode.setText("获取验证码");
                                btnVercode.setBackgroundColor(getResources().getColor(R.color.colorLoginBtnBlue));
                                btnVercode.setTextColor(getResources().getColor(R.color.color_white));
                                btnVercode.setClickable(true);
                            } else {

                                btnVercode.setText("(" + StringUtils.value(msg.arg1) + ")秒");
                                btnVercode.setBackgroundColor(getResources().getColor(R.color.colorLoginBtnGray));
                                btnVercode.setClickable(false);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

}
