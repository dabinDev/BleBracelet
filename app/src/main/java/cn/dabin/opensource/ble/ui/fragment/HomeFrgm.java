package cn.dabin.opensource.ble.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;
import com.vise.xsnow.event.BusManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseFragment;
import cn.dabin.opensource.ble.network.BleApi;
import cn.dabin.opensource.ble.network.bean.HomeData;
import cn.dabin.opensource.ble.network.bean.ResultMsg;
import cn.dabin.opensource.ble.network.bean.SaveShData;
import cn.dabin.opensource.ble.network.bean.SeatInfo;
import cn.dabin.opensource.ble.network.bean.StepInfo;
import cn.dabin.opensource.ble.network.bean.UseEyeInfo;
import cn.dabin.opensource.ble.network.callback.MineStringCallback;
import cn.dabin.opensource.ble.ui.activity.HomeAct;
import cn.dabin.opensource.ble.util.CollectionUtil;
import cn.dabin.opensource.ble.util.DateUtil;
import cn.dabin.opensource.ble.util.Logger;
import cn.dabin.opensource.ble.util.StringUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.fragment
 * Created by :  dabin.
 * Created time: 2019/8/27 16:26
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:26
 * Class description:
 */
public class HomeFrgm extends BaseFragment implements View.OnClickListener, HomeAct.BleCallBack, TimePickerView.OnTimeSelectListener {
    private final String COMMAND_SET_TIME = "st";//设置时间
    private final String COMMAND_BATTERY_INFO = "v";//电量信息
    private final String COMMAND_ALL_USE_EYE = "a";//全部用眼信息数据
    private final String COMMAND_ALL_STEP = "e";//全部步数
    private final String COMMAND_NEAR_USE_EYE = "f";//近距离用眼数据
    private final String COMMAND_LONG_TIME_SEAT = "l";//久坐数据
    //private final String COMMAND_TIME = "t";//时间
    private final String TAG = HomeFrgm.this.getClass().getName();
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
    private TextView tvHeart;
    private TimePickerView pvTime;
    private String currentCommond = "";
    private List<StepInfo> stepInfoList = new ArrayList<>();
    //全部用眼信息
    private List<UseEyeInfo> useEyeAllList = new ArrayList<>();
    //近距离用眼数据
    private List<UseEyeInfo> useEyeNearList = new ArrayList<>();
    //久坐数据
    private List<Long> longTimeSeatList = new ArrayList<>();

    private Date currentDate;
    private HomeData homeData;


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
        }
    }


    //**获取当前电量
    private void setBleTime() {
        loading("设置时间信息");
        currentCommond = COMMAND_SET_TIME;
        new Handler().postDelayed(() -> {
            ((HomeAct) getActivity()).sendMsg(COMMAND_SET_TIME + new Date().getTime());
        }, 1000);

    }

    //**获取当前电量
    private void reqBleBatteryInfo() {
        loading("读取手环数据中");
        currentCommond = COMMAND_BATTERY_INFO;
        ((HomeAct) getActivity()).sendMsg(COMMAND_BATTERY_INFO);
    }

    /**
     * 获取全部用眼信息
     */
    private void reqBleUseEyeAll() {
        loading("读取用眼信息");

        new Handler().postDelayed(() -> {
            ((HomeAct) getActivity()).sendMsg(COMMAND_ALL_USE_EYE);
            currentCommond = COMMAND_ALL_USE_EYE;
        }, 1000);
        new Handler().postDelayed(() -> {
            ((HomeAct) getActivity()).sendMsg(COMMAND_BATTERY_INFO);
        }, 2000);
    }


    /**
     * 获取步数信息
     */
    private void reqBleFootStep() {
        loading("步数信息");
        new Handler().postDelayed(() -> {
            ((HomeAct) getActivity()).sendMsg(COMMAND_ALL_STEP);
            currentCommond = COMMAND_ALL_STEP;
        }, 1000);
        new Handler().postDelayed(() -> {
            ((HomeAct) getActivity()).sendMsg(COMMAND_BATTERY_INFO);
        }, 2000);
    }

    /**
     * 获取近距离用眼信息
     */
    private void reqBleUseEyeEyeNear() {
        loading("近距离用眼信息");
        new Handler().postDelayed(() -> {
            ((HomeAct) getActivity()).sendMsg(COMMAND_NEAR_USE_EYE);
            currentCommond = COMMAND_NEAR_USE_EYE;
        }, 1000);
        new Handler().postDelayed(() -> {
            ((HomeAct) getActivity()).sendMsg(COMMAND_BATTERY_INFO);
        }, 2000);
    }

    /**
     * 获取获取久坐数据
     */
    private void reqBleLongTimeSeat() {
        loading("长时间用眼信息");
        new Handler().postDelayed(() -> {
            ((HomeAct) getActivity()).sendMsg(COMMAND_LONG_TIME_SEAT);
            currentCommond = COMMAND_LONG_TIME_SEAT;
        }, 1000);
        new Handler().postDelayed(() -> {
            ((HomeAct) getActivity()).sendMsg(COMMAND_BATTERY_INFO);
        }, 2000);
    }


    @Override public void onLazyLoad() {
        initView();
        currentDate = new Date();
        switchDate(currentDate);
    }

    @SuppressLint("StringFormatInvalid") private void initView() {
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
        tvUseDistanceUse = view.findViewById(R.id.tv_use_distance_average);
        tvHeart = view.findViewById(R.id.tv_heart);
        tvToLast.setOnClickListener(this);
        tvHomeTime.setOnClickListener(this);
        tvToNext.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(tvHeart, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        Calendar endDate = Calendar.getInstance();
        //时间选择器
        pvTime = new TimePickerView.Builder(getContext(), this)
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setBgColor(getResources().getColor(R.color.color_white))
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleText("选择日期")
                .setDividerColor(getResources().getColor(R.color.colorSecondaryText))
                .setTitleColor(getResources().getColor(R.color.colorSecondaryText))//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.colorPrimary))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.colorSecondaryText))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.colorBgGray))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.colorWhite))//滚轮背景颜色 Night mode
                .setRangDate(null, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
    }


    @Override public void onDestroy() {
        super.onDestroy();
        BusManager.getBus().unregister(this);
    }

    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_to_last:
                switchDate(DateUtil.strToDate(DateUtil.getBeforeDay(currentDate)));
                break;
            case R.id.tv_home_time:
                if (view.getTag() != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime((Date) view.getTag());
                    pvTime.setDate(calendar);
                    pvTime.show(view);
                } else {
                    pvTime.show(view);
                }
                break;
            case R.id.tv_to_next:
                switchDate(DateUtil.strToDate(DateUtil.getAfterDay(currentDate)));
                break;
            case R.id.tv_reset:
                homeData = new HomeData();//用于存储今日数据
                setBleTime();
                break;
            default:
                break;

        }
    }

    @Override public void onTimeSelect(Date date, View v) {
        switchDate(date);
    }

    private void switchDate(Date date) {
        loading("加载中");
        //设置顶部当前今日时间
        String time = new SimpleDateFormat("MM月dd日").format(date);
        currentDate = date;
        tvHomeTime.setTag(currentDate);
        tvHomeTime.setText(time);
        HttpParams params1 = new HttpParams();
        params1.put("userMobile", readMobile());
        params1.put("date", DateUtil.getStringDateShort(currentDate));
        params1.put("type", "1");
        OkGo.<String>get(BleApi.getUrl(BleApi.getShDataList)).params(params1).headers(new HttpHeaders("token", readToken())).tag(this).execute(new MineStringCallback() {
            @Override public void success(String result) {
                dissmiss();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject.optBoolean("success")) {
                        JSONArray model = jsonObject.optJSONArray("model");
                        if (model != null && model.optJSONObject(0) != null) {
                            String value = model.optJSONObject(0).optString("value");
                            JSONObject valueObject = new JSONObject(value);
                            String dianlinag = valueObject.optString("dianlinag");
                            String cuowuyongyanshichang = valueObject.optString("cuowuyongyanshichang");
                            String yongyanpingjinjuli = valueObject.optString("yongyanpingjinjuli");
                            String jiuzuo = valueObject.optString("jiuzuo");
                            long tongbuTime = valueObject.optLong("tongbuTime");
                            String jinjuliyanshichang = valueObject.optString("jinjuliyanshichang");
                            String yigongfenzhong = valueObject.optString("yigongfenzhong");
                            String zuijincuowujuli = valueObject.optString("zuijincuowujuli");
                            String baifenbi = valueObject.optString("baifenbi");
                            int step = valueObject.optInt("step");
                            String zhi = valueObject.optString("zhi");

                            setHealthByStep(step);
                            setBatteryInfo(dianlinag);
                            setLastUploadTime(System.currentTimeMillis());
                            //用眼总时长
                            tvTotalUse.setText(StringUtils.valueWithZero(yigongfenzhong));
                            //错误用眼时长
                            tvErrorUse.setText(StringUtils.valueWithZero(cuowuyongyanshichang));
                            //长时间用眼数据
                            tvLongUseTime.setText(StringUtils.valueWithZero(jiuzuo));
                            //近距离用眼数据
                            tvNearDistanceUse.setText(StringUtils.valueWithZero(jinjuliyanshichang));
                            //近距离用眼数据
                            tvUseDistanceUse.setText(StringUtils.valueWithZero(yongyanpingjinjuli));
                            //心 属性
                            tvHeart.setText(StringUtils.value(zhi));
                        } else {
                            showTopWrongMsg("该天没有数据！");
                        }
                    } else {
                        showTopWrongMsg("该天没有数据！");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dissmiss();
            }

            @Override public void onError(Response<String> response) {
                super.onError(response);
                dissmiss();
            }
        });
    }

    @Override public void receivedData(String received) {
        String msg = received.toLowerCase();
        if (currentCommond.equals(COMMAND_SET_TIME) && StringUtils.value(msg).contains("time")) {
            reqBleBatteryInfo();
        } else if (COMMAND_BATTERY_INFO.equals(currentCommond) && StringUtils.value(msg).contains("v:") && StringUtils.value(msg).contains("mv")) {
            homeData.setDianlinag(msg);//设置电量数据
            reqBleUseEyeAll();
        } else if //获取步数信息
        (StringUtils.value(msg).contains("t:") && StringUtils.value(msg).contains("b:")) {
            if (currentCommond.equals(COMMAND_ALL_STEP)) {
                StepInfo stepInfo = new StepInfo(msg);
                stepInfoList.add(stepInfo);

                String json = new Gson().toJson(stepInfoList, new TypeToken<List<StepInfo>>() {
                }.getType());
                //todo  计算健康值
            }
        } else if //获取久坐数据
        (StringUtils.value(msg).contains("l:") && StringUtils.value(msg).startsWith("l:")) {
            if (currentCommond.equals(COMMAND_LONG_TIME_SEAT)) {
                long time = Long.valueOf(msg.replaceAll("l:", "").trim());
                time = DateUtil.removeSecond(time);
                longTimeSeatList.add(time);
                //todo  计算健康值
            }
        } else if //包含全部用眼数据 和近距离用眼数据
        (StringUtils.value(msg).contains("t:") && StringUtils.value(msg).contains("r:")) {
            //获取步数信息
            UseEyeInfo useEyeInfo = new UseEyeInfo(msg);
            if (currentCommond.equals(COMMAND_ALL_USE_EYE)) {
                useEyeAllList.add(useEyeInfo);
                String json = new Gson().toJson(useEyeAllList, new TypeToken<List<UseEyeInfo>>() {
                }.getType());
                //todo 全部用眼数据
            } else if (currentCommond.equals(COMMAND_NEAR_USE_EYE)) {
                useEyeNearList.add(useEyeInfo);
                String json = new Gson().toJson(useEyeAllList, new TypeToken<List<UseEyeInfo>>() {
                }.getType());
                //todo 近距离用眼数据
            }
        } else if (StringUtils.value(msg).contains("v:") && StringUtils.value(msg).contains("mv")) {
            switch (currentCommond) {
                case COMMAND_ALL_USE_EYE:
                    reqBleFootStep();
                    break;
                case COMMAND_ALL_STEP:
                    reqBleUseEyeEyeNear();
                    break;
                case COMMAND_NEAR_USE_EYE:
                    reqBleLongTimeSeat();
                    break;
                case COMMAND_LONG_TIME_SEAT:
                    loading("同步数据中");
                    String allUseEyeJson = new Gson().toJson(useEyeAllList, new TypeToken<List<UseEyeInfo>>() {
                    }.getType());
                    String nearUseEyeJson = new Gson().toJson(useEyeNearList, new TypeToken<List<UseEyeInfo>>() {
                    }.getType());
                    String stepInfoJson = new Gson().toJson(stepInfoList, new TypeToken<List<SeatInfo>>() {
                    }.getType());
                    String longTimeSeatJson = longTimeSeatList.toString();
                    Logger.e("全部用眼信息  :", allUseEyeJson);
                    Logger.e("近距离用眼信息  :", nearUseEyeJson);
                    Logger.e("步数信息  :", stepInfoJson);
                    Logger.e("久坐信息  :", longTimeSeatJson);
                    int totalStep = 0;
                    for (int i = 0; i < CollectionUtil.getCount(stepInfoList); i++) {
                        totalStep += stepInfoList.get(i).getStep();
                    }
                    homeData.setJinjuliyanshichang(StringUtils.value(CollectionUtil.getCount(useEyeNearList)));
                    homeData.setStep(StringUtils.value(totalStep));
                    homeData.setYigongfenzhong(StringUtils.value(CollectionUtil.getCount(useEyeAllList)));
                    homeData.setJiuzuo(StringUtils.value(CollectionUtil.getCount(longTimeSeatList)));
                    homeData.setCuowuyongyanshichang(StringUtils.value(CollectionUtil.getCount(longTimeSeatList) + CollectionUtil.getCount(longTimeSeatList)));
                    homeData.setBaifenbi(StringUtils.value(CollectionUtil.isEmpty(useEyeNearList) ? "0" : CollectionUtil.getCount(useEyeNearList) / CollectionUtil.getCount(useEyeAllList)));
                    homeData.setTongbuTime(DateUtil.getStringDateShort());
                    int pingjun = 0;
                    int yongyanshijan = 0;
                    List<UseEyeInfo> todayDateList = new ArrayList<>();
                    for (int i = 0; i < CollectionUtil.getCount(useEyeAllList); i++) {
                        UseEyeInfo tempUseEye = useEyeAllList.get(i);
                        Date dateNow = new Date();
                        dateNow.setTime(tempUseEye.getTime());
                        if (DateUtil.getStringDateShort(dateNow).equals(DateUtil.getStringDateShort(new Date()))) {
                            yongyanshijan += tempUseEye.getNum();
                            todayDateList.add(tempUseEye);
                        }
                    }
                    pingjun = yongyanshijan == 0 ? 0 : yongyanshijan / CollectionUtil.getCount(todayDateList);
                    homeData.setYongyanpingjinjuli(StringUtils.value(pingjun));
                    String zhi = "";
                    if (8000 > totalStep) {
                        zhi = "不合格";
                    } else if (10000 > totalStep) {
                        zhi = "良好";
                    } else {
                        zhi = "优";
                    }
                    homeData.setZhi(zhi);
                    String homeJson = new Gson().toJson(homeData);
                    saveHomeData(1, homeJson);
                    saveHomeData(2, allUseEyeJson);
                    break;
                default:
                    dissmiss();
                    break;


            }

        }
    }

    private void saveHomeData(int type, String json) {
        loading("提交数据中");
        SaveShData shData = new SaveShData();
        shData.setUserMobile(readMobile());
        if (type == 1) {
            shData.setType("1");
            shData.setValue(json);
        } else {
            shData.setType("2");
            shData.setValue(json);
        }
        shData.setCreateTime(DateUtil.dateToStr(new Date()));
        String toJson = new Gson().toJson(shData);
        OkLogger.d("RequestBody--------------------------------------" + toJson);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        OkGo.<String>post(BleApi.getUrl(BleApi.saveShaData)).upRequestBody(requestBody).headers(new HttpHeaders("token", readToken())).tag(this).execute(new MineStringCallback() {
            @Override public void success(String result) {
                dissmiss();
                ResultMsg bean = new Gson().fromJson(result, ResultMsg.class);
                if (bean.isSuccess()) {
                    showCenterInfoMsg("提交成功");
                    switchDate(new Date());
                } else {
                    showCenterInfoMsg("提交失败");
                }
                if (type == 2) {
                    switchDate(new Date());
                }
            }

            @Override public void onError(Response<String> response) {
                super.onError(response);
                dissmiss();
                if (type == 2) {
                    switchDate(new Date());
                }
            }
        });
    }


    private void setLastUploadTime(long timeStmp) {
        String time = new SimpleDateFormat("MM月dd日 HH:mm").format(DateUtil.removeSecond(timeStmp));
        tvLastResetTime.setText(StringUtils.value("上次同步时间:" + time));
    }

    private void setHealthByStep(int step) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float value = dm.scaledDensity;
        if (8000 > step) {
            tvHeart.setText("不合格");
            tvHeart.setEms(3);
            tvHeart.invalidate();
        } else if (step > 8000 && 10000 > step) {
            tvHeart.setText("良好");
            TextViewCompat.setAutoSizeTextTypeWithDefaults(tvHeart, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            tvHeart.setEms(2);
            tvHeart.invalidate();
        } else if (step >= 10000) {
            tvHeart.setText("优");
            TextViewCompat.setAutoSizeTextTypeWithDefaults(tvHeart, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            tvHeart.setEms(1);
            tvHeart.invalidate();
        } else {
            tvHeart.setText("");
            // 参数： TextView textView, int autoSizeTextType
            TextViewCompat.setAutoSizeTextTypeWithDefaults(tvHeart, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            tvHeart.invalidate();
        }

    }

    private int setBatteryInfo(String msg) {
        String tempMsg = msg.toLowerCase().replace("mv", "").replace("v:", "");
        Double current = Double.valueOf(tempMsg);
        int battary = Double.valueOf(current / 1000).intValue();
        if (battary > 4.1) {
            tvBattery.setText(StringUtils.value("当前电量:" + 100 + "%"));
        } else if (battary < 3.6) {
            tvBattery.setText(StringUtils.value("当前电量:" + 17 + "%"));
        } else {
            battary = (int) ((battary - 3.6) * 10 * 16.6 + 17);
            tvBattery.setText(StringUtils.value("当前电量:" + battary + "%"));
        }
        return battary;
    }

    @Override public void disConnected() {


    }

    @Override public void onPause() {
        super.onPause();
        ((HomeAct) getActivity()).setCallBack(null);
    }
}
