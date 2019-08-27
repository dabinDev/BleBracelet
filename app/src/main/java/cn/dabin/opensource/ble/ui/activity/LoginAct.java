package cn.dabin.opensource.ble.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.ui.view.ImageViewWithStroke;
import cn.dabin.opensource.ble.util.SharedPreUtil;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.activity
 * Created by :  dabin.
 * Created time: 2019/8/27 15:01
 * Changed by :  dabin.
 * Changed time: 2019/8/27 15:01
 * Class description:
 */
public class LoginAct extends AppCompatActivity implements View.OnClickListener {


    private Button btnLogin;
    private LinearLayout llLoginInput;
    private EditText etUserName;
    private EditText etUserPwd;
    private RelativeLayout rlTop;
    private ImageViewWithStroke ivHeaderPic;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorMain)
                .fitsSystemWindows(true)  //使用该属性必须指定状态栏的颜色，不然状态栏透明，很难看
                .init();
        initView();
    }

    private void initView() {
        btnLogin = findViewById(R.id.btn_login);
        llLoginInput = findViewById(R.id.ll_login_input);
        etUserName = findViewById(R.id.et_user_name);
        etUserPwd = findViewById(R.id.et_user_pwd);
        rlTop = findViewById(R.id.rl_top);
        ivHeaderPic = findViewById(R.id.iv_header_pic);
        btnLogin.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                SharedPreUtil.saveString(this, "userName", etUserName.getText().toString());
                SharedPreUtil.saveString(this, "userPwd", etUserPwd.getText().toString());
                HomeAct.openAct(this);
                break;
            default:
                break;

        }
    }
}
