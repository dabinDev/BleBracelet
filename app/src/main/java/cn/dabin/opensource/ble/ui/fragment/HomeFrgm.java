package cn.dabin.opensource.ble.ui.fragment;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Date;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseFragment;
import cn.dabin.opensource.ble.event.BleEvent;
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
        EventBus.getDefault().register(this);
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
        }
    }

    public void reqNewData() {
        sendMessage("v");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BleEvent event) {
        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
        switch (event.getCurrentEvent()) {
            case BleEvent.ACTION_GATT_CONNECTED:
                reqNewData();
                break;
            case BleEvent.ACTION_DATA_AVAILABLE:
                String text = new String(event.getValue(), StandardCharsets.UTF_8).toLowerCase();
                if (text.startsWith("v:") && text.endsWith("mv")) {
                    text.replace("v:", "").replace("mv", "");
                    int battary = Integer.valueOf(text) / 1000;
                    if (battary > 4.1) {
                        tvBattery.setText(StringUtils.value("当前电量:100%"));
                    } else if (battary < 3.6) {
                        tvBattery.setText(StringUtils.value("当前电量:17%"));
                    } else {
                        battary = (int) (battary - 3.6 *10 * 16.6 +17);
                        tvBattery.setText(StringUtils.value("当前电量:" + battary + "%"));
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override public void onLazyLoad() {
        initView();
    }

    private void initView() {
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
    }


    @Override public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
