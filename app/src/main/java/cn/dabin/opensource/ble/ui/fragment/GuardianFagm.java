package cn.dabin.opensource.ble.ui.fragment;

import android.graphics.Typeface;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

import am.widget.circleprogressbar.CircleProgressBar;
import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseFragment;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.fragment
 * Created by :  dabin.
 * Created time: 2019/8/27 16:30
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:30
 * Class description:
 */
public class GuardianFagm extends BaseFragment {
    private CircleProgressBar circleprogressbarCpbDemo;
    private BarChart chart;
    private TextView tvUnnormal;
    private TextView tvLowDistance;
    private TextView tvNormal;
    private TextView tvErrorUse;
    private Typeface mTf;
    private BarData barData;

    @Override protected int getLayoutId() {
        return R.layout.frgm_guardian;
    }

    @Override public void onLazyLoad() {
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
        barData=generateDataBar(0);
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
            if (chart != null) {
                chart.animateY(700);
            }

        }
    }


    private void initChart() {

        // apply styling
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        barData.setValueTypeface(mTf);

        // set data
        chart.setData((BarData) barData);
        chart.setFitBars(true);

        // do not forget to refresh the chart
//        chart.invalidate();
        chart.animateY(700);

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
}
