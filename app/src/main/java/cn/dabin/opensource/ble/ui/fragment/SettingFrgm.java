package cn.dabin.opensource.ble.ui.fragment;

import android.os.Handler;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseFragment;
import cn.dabin.opensource.ble.global.BleApplication;
import cn.dabin.opensource.ble.network.bean.BleInfo;
import cn.dabin.opensource.ble.ui.activity.HomeAct;
import cn.dabin.opensource.ble.util.Logger;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.fragment
 * Created by :  dabin.
 * Created time: 2019/8/27 16:32
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:32
 * Class description:
 */
public class SettingFrgm extends BaseFragment implements RadioGroup.OnCheckedChangeListener, HomeAct.BleCallBack {
    private TextView tvDistanceValue;
    private RadioGroup rgDistance;
    private RadioButton radioDistance27;
    private RadioButton radioDistance31;
    private RadioButton radioDistance33;
    private TextView tvShockValue;
    private RadioGroup rgShock;
    private RadioButton radioShockLow;
    private RadioButton radioShockMiddle;
    private RadioButton radioShockHeigh;
    private TextView tvTipTimeValue;
    private RadioGroup rgTipTime;
    private RadioButton radioTipTimeLow;
    private RadioButton radioTipTimeMiddle;
    private RadioButton radioTipTimeHeigh;
    private Button btnDoSet;
    private BleInfo info;
    private String currentDistance;
    private String currentTipTime;
    private String currentShock;
    private String currentCommond = "";

    @Override protected int getLayoutId() {
        return R.layout.frgm_setting;
    }

    @Override public void onLazyLoad() {
        initView();
        info = BleApplication.getBleInfo(readMac());
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (this.getActivity() == null) {
                return;
            }
            ImmersionBar.with(this)
                    .statusBarView(R.id.view)
                    .statusBarColor(R.color.colorWhite)
                    .navigationBarEnable(false)
                    .statusBarDarkFont(true)
                    .init();
            ((HomeAct) getActivity()).setCallBack(this);
        }
    }

    private void initView() {
        tvDistanceValue = view.findViewById(R.id.tv_distance_value);
        rgDistance = view.findViewById(R.id.rg_distance);
        tvShockValue = view.findViewById(R.id.tv_shock_value);
        rgShock = view.findViewById(R.id.rg_shock);
        tvTipTimeValue = view.findViewById(R.id.tv_tip_time_value);
        rgTipTime = view.findViewById(R.id.rg_tip_time);
        btnDoSet = view.findViewById(R.id.btn_do_set);
        rgDistance.setOnCheckedChangeListener(this);
        rgShock.setOnCheckedChangeListener(this);
        rgTipTime.setOnCheckedChangeListener(this);
        btnDoSet.setOnClickListener(v ->
        {
            loading("设置中");
            setProperty();
        });
    }

    @Override public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getId()) {
            case R.id.rg_distance:

                break;
            case R.id.rg_shock:

                break;
            case R.id.rg_tip_time:

                break;
            default:
                break;

        }
    }

    private void setProperty() {
        RadioButton rg1 = (rgDistance.findViewById(rgDistance.getCheckedRadioButtonId()));
        if (rg1 != null) {
            currentDistance = String.valueOf(rg1.getTag());
        }
        RadioButton rg2 = (rgShock.findViewById(rgShock.getCheckedRadioButtonId()));
        if (rg2 != null) {
            currentShock = String.valueOf(rg2.getTag());
        }
        RadioButton rg3 = (rgTipTime.findViewById(rgTipTime.getCheckedRadioButtonId()));
        if (rg3 != null) {
            currentTipTime = String.valueOf(rg3.getTag());
        }
        new Handler().postDelayed(() -> {
            ((HomeAct) getActivity()).sendMsg("v");
            currentCommond = "v";
        }, 500);
        //设置振动强度：(设定范围0~10)
        //	“sp”+”强度值”    默认值：10
        new Handler().postDelayed(() -> {
            ((HomeAct) getActivity()).sendMsg("sp" + currentShock);
            currentCommond = "sp";
        }, 1000);
        //22.设置距离监测下持续检测有效时间 (范围0~60)
        //	“sr”+”时间”      默认：5 （秒）
        new Handler().postDelayed(() -> {
            ((HomeAct) getActivity()).sendMsg("sr" + currentTipTime);
            currentCommond = "sr";
        }, 1500);
    }

    @Override public void receivedData(String msg) {
        Logger.e("commond result", msg);
        switch (currentCommond) {
            case "v":
                if (msg.contains("")) {

                }
                break;
            case "sp":
                if (msg.contains("pwm set done")) {
                    Logger.e("commond" + currentCommond, msg);
                } else {
                    Logger.e("commond" + currentCommond, "设置失败!");
                }
                break;
            case "sr":
                if (msg.contains("remind set done")) {
                    Logger.e("commond" + currentCommond, msg);
                } else {
                    Logger.e("commond" + currentCommond, "设置失败!");
                }
                dissmiss();
                break;
            default:
                break;

        }
    }

    @Override public void disConnected() {

    }
}
