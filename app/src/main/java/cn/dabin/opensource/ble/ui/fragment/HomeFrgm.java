package cn.dabin.opensource.ble.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.vise.xsnow.event.BusManager;
import com.vise.xsnow.event.Subscribe;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseFragment;
import cn.dabin.opensource.ble.event.NotifyDataEvent;
import cn.dabin.opensource.ble.global.BleApplication;
import cn.dabin.opensource.ble.network.bean.BleInfo;
import cn.dabin.opensource.ble.ui.activity.HomeAct;
import cn.dabin.opensource.ble.util.StringUtils;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.fragment
 * Created by :  dabin.
 * Created time: 2019/8/27 16:26
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:26
 * Class description:
 */
public class HomeFrgm extends BaseFragment {
    private RelativeLayout topPanel;
    private LinearLayout llTop1;
    private ImageView tvToLast;
    private TextView tvHomeTime;
    private ImageView tvToNext;
    private RelativeLayout rlTop2;
    private RelativeLayout rlTop3;
    private TextView tvTodayStatus;
    private TextView tvTodayTime;
    private TextView tvTodayFlag;
    private TextView tvBattery;
    private TextView tvLastResetTime;
    private TextView tvReset;
    private TextView tvTotalUse;
    private TextView tvNearDistanceUse;
    private TextView tvErrorUse;
    private TextView tvLongUseTime;
    private TextView tvUseDistanceUse;

    @Override protected int getLayoutId() {
        BusManager.getBus().register(this);
        return R.layout.frgm_home;
    }


    @Override public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ImmersionBar.with(this)
                    .statusBarView(R.id.topPanel)
                    .statusBarColor(R.color.colorStatusBarBlue)
                    .statusBarDarkFont(false)
                    .fullScreen(true)
                    .init();
            reqBleData();
        }
    }

    private void reqBleData() {
        ((HomeAct) getActivity()).sendMsg("v");
    }

    @SuppressLint("StringFormatInvalid") @Subscribe
    public void showDeviceNotifyData(NotifyDataEvent event) throws UnsupportedEncodingException {
        if (event != null && event.getData() != null && event.getBluetoothLeDevice() != null) {
            String msg = new String(event.getData(), "GBK").toLowerCase();
            if (StringUtils.value(msg).contains("v:") && StringUtils.value(msg).contains("mv")) {
                msg = msg.replace("mv", "").replace("v:", "");
                double battary = Double.valueOf(msg) / 1000;
                if (battary > 4.1) {
                    tvBattery.setText(StringUtils.value("当前电量:" + 100 + "%"));
                } else if (battary < 3.6) {
                    tvBattery.setText(StringUtils.value("当前电量:" + 17 + "%"));
                } else {
                    battary = (battary - 3.6) * 10 * 16.6 + 17;
                    tvBattery.setText(StringUtils.value("当前电量:" + battary + "%"));
                }
                String time = DateFormat.getTimeInstance().format(new Date());
                tvLastResetTime.setText(StringUtils.value("上次同步时间:" + time));
                BleInfo info = BleApplication.getBleInfo(readMac());
                if (info == null) {
                    info = new BleInfo();
                }
                info.setCurrentBattery((int) battary);
                info.save();
            }

        }
    }


    @Override public void onLazyLoad() {
        initView();
    }

    @SuppressLint("StringFormatInvalid") private void initView() {
        topPanel = view.findViewById(R.id.topPanel);
        tvToLast = view.findViewById(R.id.tv_to_last);
        tvHomeTime = view.findViewById(R.id.tv_home_time);
        tvToNext = view.findViewById(R.id.tv_to_next);
        tvTodayStatus = view.findViewById(R.id.tv_today_status);
        tvTodayTime = view.findViewById(R.id.tv_today_time);
        tvTodayFlag = view.findViewById(R.id.tv_today_flag);
        tvBattery = view.findViewById(R.id.tv_battery);
        tvLastResetTime = view.findViewById(R.id.tv_last_reset_time);
        tvReset = view.findViewById(R.id.tv_reset);
        tvTotalUse = view.findViewById(R.id.tv_total_use);
        tvNearDistanceUse = view.findViewById(R.id.tv_near_distance_use);
        tvErrorUse = view.findViewById(R.id.tv_error_use);
        tvLongUseTime = view.findViewById(R.id.tv_long_use_time);
        tvUseDistanceUse = view.findViewById(R.id.tv_use_distance_use);
        String time= DateFormat.getTimeInstance(DateFormat.FULL, Locale.SIMPLIFIED_CHINESE).format(new Date());

        tvTodayTime.setText(time);
        if (StringUtils.isNotEmpty(readMac())) {
            BleInfo info = BleApplication.getBleInfo(readMac());
            tvBattery.setText(StringUtils.value("当前电量:" + info.getCurrentBattery() + "%"));
        }

    }


    @Override public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
