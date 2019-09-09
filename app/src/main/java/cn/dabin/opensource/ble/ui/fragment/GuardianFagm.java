package cn.dabin.opensource.ble.ui.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import am.widget.circleprogressbar.CircleProgressBar;
import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.adapter.XValueFormatter;
import cn.dabin.opensource.ble.adapter.YValueBarDataSet;
import cn.dabin.opensource.ble.base.BaseFragment;
import cn.dabin.opensource.ble.network.BleApi;
import cn.dabin.opensource.ble.network.bean.ResultMsg;
import cn.dabin.opensource.ble.network.callback.MineStringCallback;
import cn.dabin.opensource.ble.util.DateUtil;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.fragment
 * Created by :  dabin.
 * Created time: 2019/8/27 16:30
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:30
 * Class description:
 */
public class GuardianFagm extends BaseFragment implements View.OnClickListener {
    String[] arrs5_7 = {"07:05", "07:10", "07:15", "07:20", "07:25", "07:30", "07:35", "07:40", "07:45", "07:50", "07:55", "08:00", "08:05", "08:10", "08:15", "08:20", "08:25", "08:30", "08:35", "08:40", "08:45", "08:50", "08:55", "09:00", "09:05", "09:10", "09:15", "09:20", "09:25", "09:30", "09:35", "09:40", "09:45", "09:50", "09:55", "10:00", "10:05", "10:10", "10:15", "10:20", "10:25", "10:30", "10:35", "10:40", "10:45", "10:50", "10:55", "11:00", "11:05", "11:10", "11:15", "11:20", "11:25", "11:30", "11:35", "11:40", "11:45", "11:50", "11:55", "12:00"};
    String[] arrs12_17 = {"12:05", "12:10", "12:15", "12:20", "12:25", "12:30", "12:35", "12:40", "12:45", "12:50", "12:55", "13:00", "13:05", "13:10", "13:15", "13:20", "13:25", "13:30", "13:35", "13:40", "13:45", "13:50", "13:55", "14:00", "14:05", "14:10", "14:15", "14:20", "14:25", "14:30", "14:35", "14:40", "14:45", "14:50", "14:55", "15:00", "15:05", "15:10", "15:15", "15:20", "15:25", "15:30", "15:35", "15:40", "15:45", "15:50", "15:55", "16:00", "16:05", "16:10", "16:15", "16:20", "16:25", "16:30", "16:35", "16:40", "16:45", "16:50", "16:55", "17:00"};
    String[] arrs17_22 = {"17:05", "17:10", "17:15", "17:20", "17:25", "17:30", "17:35", "17:40", "17:45", "17:50", "17:55", "18:00", "18:05", "18:10", "18:15", "18:20", "18:25", "18:30", "18:35", "18:40", "18:45", "18:50", "18:55", "19:00", "19:05", "19:10", "19:15", "19:20", "19:25", "19:30", "19:35", "19:40", "19:45", "19:50", "19:55", "20:00", "20:05", "20:10", "20:15", "20:20", "20:25", "20:30", "20:35", "20:40", "20:45", "20:50", "20:55", "21:00", "21:05", "21:10", "21:15", "21:20", "21:25", "21:30", "21:35", "21:40", "21:45", "21:50", "21:55", "22:00"};

    private ArrayList<String> xList5_7 = new ArrayList<>();
    private ArrayList<String> xList12_17 = new ArrayList<>();
    private ArrayList<String> xList17_22 = new ArrayList<>();


    private CircleProgressBar circleprogressbarCpbDemo;
    private BarChart chart;
    private TextView tvUnnormal;
    private TextView tvLowDistance;
    private TextView tvNormal;
    private TextView tvErrorUse;
    private Typeface mTf;
    private BarData barData;
    private TextView tvMeasure;
    private ArrayList<String> xListValue;
    private ArrayList<Integer> yListValue;

    @Override protected int getLayoutId() {
        return R.layout.frgm_guardian;
    }

    @Override public void onLazyLoad() {
        xList5_7.addAll(Arrays.asList(arrs5_7));
        xList12_17.addAll(Arrays.asList(arrs12_17));
        xList17_22.addAll(Arrays.asList(arrs17_22));
        initView();
        initChart();
    }


    private void initView() {
        circleprogressbarCpbDemo = view.findViewById(R.id.circleprogressbar_cpb_demo);
        chart = view.findViewById(R.id.chart);
        tvUnnormal = view.findViewById(R.id.tv_unnormal);
        tvLowDistance = view.findViewById(R.id.tv_low_distance);
        tvNormal = view.findViewById(R.id.tv_normal);
        tvErrorUse = view.findViewById(R.id.tv_error_use);
        mTf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
        tvMeasure = view.findViewById(R.id.tv_measure);
        view.findViewById(R.id.tv_5_7).setOnClickListener(this);
        view.findViewById(R.id.tv_12_17).setOnClickListener(this);
        view.findViewById(R.id.tv_17_22).setOnClickListener(this);
        xListValue = new ArrayList<>();
        xListValue.addAll(Arrays.asList(arrs5_7));

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
            if (chart != null) {
                chart.animateY(700);
            }

        }
    }

    private void initData(Date date) {
        HttpParams params1 = new HttpParams();
        params1.put("userMobile", readMobile());
        params1.put("date", DateUtil.dateToStr(date));
        params1.put("type", "2");
        OkGo.<String>get(BleApi.getUrl(BleApi.getShDataList)).params(params1).headers(new HttpHeaders("token", readToken())).tag(this).execute(new MineStringCallback() {
            @Override public void success(String result) {
                dissmiss();
                ResultMsg bean = new Gson().fromJson(result, ResultMsg.class);
                if (bean.isSuccess()) {
                    String modol = bean.getModel().toString();
                } else {
                    showCenterInfoMsg("提交失败");
                }
            }

            @Override public void onError(Response<String> response) {
                super.onError(response);
                dissmiss();
            }
        });
    }

    private void initChart() {
        chart.setDrawBarShadow(false);//设置每个直方图阴影为false
        chart.setDrawValueAboveBar(false);//这里设置为true每一个直方图的值就会显示在直方图的顶部
        chart.getDescription().setEnabled(false);//设置描述不显示
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);//设置不显示网格
        chart.setBackgroundColor(Color.parseColor("#F3F3F3"));//设置图表的背景颜色
        //chart.setMaxVisibleValueCount(60);
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
        xAxis.setLabelRotationAngle(-40f);//设置X轴字体显示角度

        //左边Y轴
        YAxis leftYAxis = chart.getAxisLeft();
        leftYAxis.setDrawGridLines(true);//设置从Y轴左侧发出横线
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setEnabled(true);//设置显示左边Y坐标
        //右边Y轴
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);//设置隐藏右边y坐标
        yListValue = new ArrayList<>();
        for (int i = 0; i < xListValue.size(); i++) {
            int y = new Random().nextInt(50);
            yListValue.add(y);
        }
        setData(xListValue.size(), yListValue);
    }


    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));

        }
        BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
        d.setColors(ColorTemplate.JIANKONG_COLORS);
        d.setHighLightAlpha(255);
        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_5_7:
                xListValue = new ArrayList<>();
                break;
            case R.id.tv_12_17:
                xListValue = new ArrayList<>();
                break;
            case R.id.tv_17_22:
                xListValue = new ArrayList<>();
                break;

        }
    }

    private void setData(int count, ArrayList<Integer> list) {
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            yValues.add(new BarEntry(i, list.get(i)));
           /* int distance = list.get(i);
            Logger.e("print ====color", StringUtils.value(distance));
            if (distance < 100) {
                colors.add(R.color.colorAbnormal);//非正常
            } else if (distance > 100 && distance < 320) {
                colors.add(R.color.colorNearly);//非正常
            } else if (distance > 320 && distance < 500) {
                colors.add(R.color.colorNormal);//正常使用
            } else if (distance > 500 && distance < 8191) {
                colors.add(R.color.colorWrong);//错误使用
            } else {
                colors.add(R.color.colorSecondaryText);
            }*/
        }
        YValueBarDataSet set;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set = (YValueBarDataSet) chart.getData().getDataSetByIndex(0);
            set.setValues(yValues);
            set.setColors(R.color.color_red,R.color.colorNearly,R.color.colorNormal,R.color.colorWrong,R.color.colorSecondaryText);
            set.setHighLightAlpha(255);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set = new YValueBarDataSet(yValues, "");
            set.setDrawIcons(false);//设置直方图上面时候显示图标
            set.setColors(R.color.color_red,R.color.colorNearly,R.color.colorNormal,R.color.colorWrong,R.color.colorSecondaryText);
            set.setHighLightAlpha(255);
// YValueBarDataSet yValueBarDataSet = new YValueBarDataSet(yValues, "");
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);//设置直方图上面文字的大小
            data.setBarWidth(0.9f);//设置直方图的宽度
            data.setValueTextColor(Color.parseColor("#ffffff"));//设置直方图顶部显示Y值的颜色
            chart.setData(data);//设置值
            chart.invalidate();
        }
    }


}
