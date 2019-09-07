package cn.dabin.opensource.ble.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gyf.immersionbar.ImmersionBar;
import com.vise.xsnow.event.BusManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseFragment;
import cn.dabin.opensource.ble.global.BleApplication;
import cn.dabin.opensource.ble.network.bean.BleInfo;
import cn.dabin.opensource.ble.ui.activity.HomeAct;
import cn.dabin.opensource.ble.util.StringUtils;

import static com.vise.utils.handler.HandlerUtil.runOnUiThread;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.fragment
 * Created by :  dabin.
 * Created time: 2019/8/27 16:26
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:26
 * Class description:
 */
public class HomeFrgm extends BaseFragment implements View.OnClickListener, OnTimeSelectListener, HomeAct.BleCallBack {
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
    private TimePickerView pvTime;
    private String currentMsgTag = "";
    private final String TAG = HomeFrgm.this.getClass().getName();


    @Override protected int getLayoutId() {
        BusManager.getBus().register(this);
        return R.layout.frgm_home;
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (this.getActivity() == null) {
                return;
            }
            ImmersionBar.with(this)
                    .statusBarView(R.id.view)
                    .statusBarColor(R.color.colorStatusBarBlue)
                    .navigationBarEnable(false)
                    .init();
            ((HomeAct) getActivity()).setCallBack(this);
            reqBleData();

        }
    }

    private void reqBleData() {
        ((HomeAct) getActivity()).sendMsg("a");
        new Handler().postDelayed(() -> ((HomeAct) getActivity()).sendMsg("v"), 200);

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
        tvToLast.setOnClickListener(this);
        tvHomeTime.setOnClickListener(this);
        tvToNext.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        //时间选择器
        pvTime = new TimePickerBuilder(getContext(), this)
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setBgColor(getResources().getColor(R.color.color_white))
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(18)//设置滚轮中间文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("Title")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleText("选择日期")
                .setDividerColor(getResources().getColor(R.color.colorSecondaryText))
                .setTitleColor(getResources().getColor(R.color.colorSecondaryText))//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.colorPrimary))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.colorSecondaryText))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.colorDividerGray))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.colorBg))//滚轮背景颜色 Night mode
                //.setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        if (StringUtils.isNotEmpty(readMac())) {
            BleInfo info = BleApplication.getBleInfo(readMac());
            tvBattery.setText(StringUtils.value("当前电量:" + info.getCurrentBattery() + "%"));

        }

    }


    @Override public void onDestroy() {
        super.onDestroy();
        BusManager.getBus().unregister(this);
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_to_last:
                break;
            case R.id.tv_home_time:
                pvTime.show();
                break;
            case R.id.tv_to_next:
                break;
            case R.id.tv_reset:
                reqBleData();
                break;
            default:
                break;

        }
    }

    @Override public void onTimeSelect(Date date, View v) {

    }

    private void setCurrentDate(Date date) {
        String time = new SimpleDateFormat("MM月dd日").format(date);
        tvHomeTime.setText(time);
        //todo

    }

    @Override public void receivedData(String received) {
        String msg = received.toLowerCase();
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
            String time = new SimpleDateFormat("MM月dd日 HH:mm").format(new Date());
            tvLastResetTime.setText(StringUtils.value("上次同步时间:" + time));
            double finalBattary = battary;
            runOnUiThread(() -> {
                BleInfo info = BleApplication.getBleInfo(readMac());
                if (info == null) {
                    info = new BleInfo();
                }
                info.setCurrentBattery((int) finalBattary);
                info.save();
            });
        }
    }

    @Override public void disConnected() {


    }
}
