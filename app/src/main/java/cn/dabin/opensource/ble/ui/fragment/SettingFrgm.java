package cn.dabin.opensource.ble.ui.fragment;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseFragment;
import cn.dabin.opensource.ble.global.BleApplication;
import cn.dabin.opensource.ble.network.bean.BleInfo;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.fragment
 * Created by :  dabin.
 * Created time: 2019/8/27 16:32
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:32
 * Class description:
 */
public class SettingFrgm extends BaseFragment {
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
            if (this.getActivity()==null)
            {
                return;
            }
            ImmersionBar.with(this)
                    .statusBarView(R.id.view)
                    .statusBarColor(R.color.colorWhite)
                    .navigationBarEnable(false)
                    .statusBarDarkFont(true)
                    .init();


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
    }
}
