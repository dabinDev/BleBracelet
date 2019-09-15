package cn.dabin.opensource.ble.ui.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.adapter.XValueFormatter;
import cn.dabin.opensource.ble.base.BaseFragment;
import cn.dabin.opensource.ble.network.BleApi;
import cn.dabin.opensource.ble.network.callback.MineStringCallback;
import cn.dabin.opensource.ble.util.DateUtil;
import cn.dabin.opensource.ble.util.Logger;
import cn.dabin.opensource.ble.util.StringUtils;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.fragment
 * Created by :  dabin.
 * Created time: 2019/8/27 16:31
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:31
 * Class description:
 */
public class DataFagm extends BaseFragment implements TimePickerView.OnTimeSelectListener, View.OnClickListener {
    private Typeface mTf;
    private TextView tvTip;
    private LinearLayout llTop1;
    private TextView tvEyeDistance;
    private TextView tvNearUseDate;
    private LineChart chart1;
    private LinearLayout llTop2;
    private TextView tvErrorUseDate;
    private LineChart chart2;
    private TimePickerView pvTime;
    private ArrayList<String> xListValue1 = DateUtil.getDayByMonth(new Date(), "MM.dd");
    private ArrayList<String> xListValue2 = DateUtil.getDayByMonth(new Date(), "MM.dd");
    private ArrayList<Integer> yListValue1 = new ArrayList<>();
    private ArrayList<Integer> yListValue2 = new ArrayList<>();
    private Date currentDate;


    @Override protected int getLayoutId() {
        return R.layout.frgm_data;
    }

    @Override public void onLazyLoad() {
        initView();
        currentDate = new Date();
        Logger.e("xListValue1  ", xListValue1.toString());
        Logger.e("xListValue2  ", xListValue2.toString());
        initChart(chart1, xListValue1, 33);
        initChart(chart2, xListValue2, 50);
        switchDate(1, new Date());
        switchDate(2, new Date());
    }

    private void initView() {
        tvTip = view.findViewById(R.id.tv_tip);
        llTop1 = view.findViewById(R.id.ll_top1);
        tvEyeDistance = view.findViewById(R.id.tv_eye_distance);
        tvNearUseDate = view.findViewById(R.id.tv_near_use_date);
        chart1 = view.findViewById(R.id.chart1);
        llTop2 = view.findViewById(R.id.ll_top2);
        tvErrorUseDate = view.findViewById(R.id.tv_error_use_date);
        chart2 = view.findViewById(R.id.chart2);
        tvNearUseDate.setOnClickListener(this);
        tvErrorUseDate.setOnClickListener(this);
        Calendar endDate = Calendar.getInstance();
        //时间选择器
        pvTime = new TimePickerView.Builder(getContext(), this)
                .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
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
            if (chart1 != null) {
                chart1.animateY(700);
            }
            if (chart2 != null) {
                chart2.animateY(700);
            }

        }
    }


    private void initChart(LineChart chart, ArrayList<String> xListValue, int type) {
        mTf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
        chart.setNoDataText("暂无数据");
        chart.getDescription().setEnabled(false);//设置描述不显示
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);//设置不显示网格
        chart.setBackgroundColor(Color.parseColor("#F3F3F3"));//设置图表的背景颜色
        chart.getXAxis().setDrawGridLines(true);//是否显示竖直标尺线//chart.setMaxVisibleValueCount(60);
        Description description = new Description();
        description.setText(type == 1 ? "单位(/分钟)" : "单位(/百分之)");
        description.setPosition(chart.getX(), chart.getY());
        chart.setDescription(description);// 数据描述
//chart.setMaxVisibleValueCount(60);
        //设置均值
        LimitLine ll2 = new LimitLine(type == 1 ? 33 : 50, "正常值");
        ll2.setLabel("均值");
        ll2.setTextColor(type == 1 ? getResources().getColor(R.color.color_red) : getResources().getColor(R.color.color_yellow));
        ll2.setLineWidth(1f);
        ll2.setEnabled(true);
        ll2.setLineColor(Color.parseColor("#5dbcfe"));
        ll2.enableDashedLine(5f, 10f, 0f);//三个参数，第一个线宽长度，第二个线段之间宽度，第三个一般为0，是个补偿
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);//标签位置
        ll2.setTextSize(10f);
        chart.getAxisLeft().addLimitLine(ll2);

        //通过下面两句代码实现左右滚动
        float ratio = (float) xListValue.size() / (float) 10;//我默认手机屏幕上显示10剩下的滑动直方图然后显示。假如要显示25个 那么除以10 就是放大2.5f
        chart.zoom(ratio, 1f, 0, 0);//显示的时候是按照多大的比率缩放显示  1f表示不放大缩小

        chart.animateY(1500);//从Y轴弹出的动画时间
        chart.getLegend().setEnabled(false);//设置不显示比例图
        chart.setScaleEnabled(false);//设置是否可以缩放
        chart.setTouchEnabled(true);//设置是否可以触摸
        chart.setDragEnabled(true);//设置是否可以拖拽

        //自定义设置横坐标
        XValueFormatter xValueFormatter = new XValueFormatter(xListValue);
        //设置不显示网格线，保留水平线
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。
        //xAxis.setLabelCount(xListValue.size());//设置横坐标显示多少
        xAxis.setLabelCount(10);//一个界面显示10个Lable。那么这里要设置11个
        xAxis.setValueFormatter(xValueFormatter);//将自定义的横坐标设置上去
        //xAxis.setLabelRotationAngle(-40f);//设置X轴字体显示角度
        //左边Y轴
        YAxis leftYAxis = chart.getAxisLeft();
        leftYAxis.setDrawGridLines(true);//设置从Y轴左侧发出横线
        leftYAxis.setSpaceMin(10);
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setEnabled(true);//设置显示左边Y坐标
        //右边Y轴
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);//设置隐藏右边y坐标
        // do not forget to refresh the chart
        // holder.chart.invalidate();
        chart.animateX(750);
    }


    @Override public void onTimeSelect(Date date, View v) {
        switch (v.getId()) {
            case R.id.tv_near_use_date:
                switchDate(1, date);
                break;
            case R.id.tv_error_use_date:
                switchDate(2, date);
                break;
            default:
                break;
        }
    }


    /**
     * @param type 代表图表1或者图表2
     * @param date
     */
    private void switchDate(int type, Date date) {
        loading("加载中");
        //设置顶部当前今日时间
        String time = new SimpleDateFormat("yyyy.MM").format(date);
        String month = new SimpleDateFormat("MM.").format(date);
        if (type == 1) {
            tvNearUseDate.setText(time);
            tvNearUseDate.setTag(date);
        } else {
            tvErrorUseDate.setText(time);
            tvErrorUseDate.setTag(date);
        }
        currentDate = date;
        HttpParams params1 = new HttpParams();
        params1.put("userMobile", readMobile());
        params1.put("date", DateUtil.getStringDateShort(date));
        params1.put("type", type);
        OkGo.<String>get(BleApi.getUrl(BleApi.getShYongyan)).params(params1).headers(new HttpHeaders("token", readToken())).tag(this).execute(new MineStringCallback() {
            @Override public void success(String result) {
                dissmiss();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject model = jsonObject.optJSONObject("model");
                        if (model != null) {
                            for (int i = 0; i < xListValue1.size(); i++) {
                                String index = xListValue1.get(i);
                                index = index.contains(month) ? index.replace(month, "") : index;
                                index = index.startsWith("0") ? index.replace("0", "") : index;
                                if (type == 1) {
                                    if (model.optJSONArray(index) != null && model.optJSONArray(index).opt(0) != null) {
                                        JSONObject arrayObject = model.optJSONArray(index).optJSONObject(0);
                                        int shijian = arrayObject.optInt("yongyanshijian");
                                        if (shijian > 1 && shijian < 100) {
                                            shijian = 100;
                                        } else if (shijian < 8192 && shijian > 700) {
                                            shijian = 700;
                                        }
                                        yListValue1.add(shijian);
                                    } else {
                                        yListValue1.add(0);
                                    }
                                } else {
                                    int baifenbi = 0;
                                    if (model.optJSONArray(index) != null && model.optJSONArray(index).opt(0) != null) {
                                        JSONObject arrayObject = model.optJSONArray(index).optJSONObject(0);
                                        String shijian = arrayObject.optString("yongyanshijian");
                                        if (StringUtils.isNotEmpty(shijian)) {
                                            JSONObject shijianObject = new JSONObject(shijian);
                                            double cuo = shijianObject.optDouble("cuo");
                                            double yi = shijianObject.optDouble("yi");
                                            baifenbi = (int) (cuo / yi * 100);
                                        }
                                        yListValue2.add(baifenbi);
                                    } else {
                                        yListValue2.add(baifenbi);
                                    }
                                }
                            }
                            if (type == 1) {
                                setData(chart1, xListValue1.size(), yListValue1);
                            } else {
                                setData(chart2, xListValue2.size(), yListValue2);
                            }

                        } else {
                            showTopWrongMsg("该月份没有数据！");
                        }
                    } else {
                        showTopWrongMsg("该月份没有数据！");
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


    private void setData(LineChart chart, int count, ArrayList<Integer> list) {
        chart.invalidate();
        ArrayList<Entry> yValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            yValues.add(new Entry(i, list.get(i)));
        }
        LineDataSet set;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set = new LineDataSet(yValues, "");
            set.setLineWidth(2.5f);
            set.setCircleRadius(4.5f);
            set.setColor(getResources().getColor(R.color.colorSecondaryText));
            set.setCircleColor(getResources().getColor(R.color.colorWrong));
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setVisible(true);//是否显示柱状图柱子
            set.setDrawValues(true);//是否显示柱子上面的数值
        } else {
            set = new LineDataSet(yValues, "");
            set.setDrawIcons(false);//设置直方图上面时候显示图标
            set.setLineWidth(2.5f);
            set.setCircleRadius(4.5f);
            set.setColor(getResources().getColor(R.color.colorSecondaryText));
            set.setCircleColor(getResources().getColor(R.color.colorWrong));
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setVisible(true);//是否显示柱状图柱子
            set.setDrawValues(true);//是否显示柱子上面的数值
            ArrayList<ILineDataSet> iLineDS = new ArrayList<ILineDataSet>();
            iLineDS.add(set);
            LineData data = new LineData(iLineDS);
            data.setDrawValues(true);
            data.setValueTypeface(mTf);
            data.setValueTextSize(10f);//设置直方图上面文字的大小
            data.setValueTextColor(getResources().getColor(R.color.colorSecondaryText));//设置直方图顶部显示Y值的颜色
            data.setValueFormatter(new ValueFormatter() {
                @Override public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });
            chart.setData(data);//设置值
            chart.invalidate();
        }
    }


    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_near_use_date:
                if (view.getTag() != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime((Date) view.getTag());
                    pvTime.setDate(calendar);
                    pvTime.show(view);
                } else {
                    pvTime.show(view);
                }
                break;
            case R.id.tv_error_use_date:
                if (view.getTag() != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime((Date) view.getTag());
                    pvTime.setDate(calendar);
                    pvTime.show(view);
                } else {
                    pvTime.show(view);
                }
                break;
            default:
                break;
        }
    }


}
