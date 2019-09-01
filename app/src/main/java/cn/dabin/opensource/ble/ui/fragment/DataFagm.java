package cn.dabin.opensource.ble.ui.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseFragment;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.fragment
 * Created by :  dabin.
 * Created time: 2019/8/27 16:31
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:31
 * Class description:
 */
public class DataFagm extends BaseFragment {
    private Typeface mTf;
    private TextView tvEyeDistance;
    private LineChart chart1;
    private TextView tvErrorTime;
    private LineChart chart2;

    @Override protected int getLayoutId() {
        return R.layout.frgm_data;
    }

    @Override public void onLazyLoad() {
        initView();
    }

    private void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.color_white)
                .statusBarDarkFont(true)
                .fullScreen(true)
                .init();
        tvEyeDistance = view.findViewById(R.id.tv_eye_distance);
        chart1 = view.findViewById(R.id.chart1);
        tvErrorTime = view.findViewById(R.id.tv_error_time);
        chart2 = view.findViewById(R.id.chart2);
        initChart();
        initTestData();
    }



    private void initChart() {
        mTf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");

        // apply styling
        // holder.chart.setValueTypeface(mTf);
        chart1.getDescription().setEnabled(false);
        chart1.setDrawGridBackground(false);

        chart2.getDescription().setEnabled(false);
        chart2.setDrawGridBackground(false);

        XAxis xAxis1 = chart1.getXAxis();
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setTypeface(mTf);
        xAxis1.setDrawGridLines(false);
        xAxis1.setDrawAxisLine(true);

        XAxis xAxis2 = chart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setTypeface(mTf);
        xAxis2.setDrawGridLines(false);
        xAxis2.setDrawAxisLine(true);

        YAxis leftAxis1 = chart1.getAxisLeft();
        leftAxis1.setTypeface(mTf);
        leftAxis1.setLabelCount(5, false);
        leftAxis1.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis2 = chart2.getAxisLeft();
        leftAxis2.setTypeface(mTf);
        leftAxis2.setLabelCount(5, false);
        leftAxis2.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis1 = chart1.getAxisRight();
        rightAxis1.setTypeface(mTf);
        rightAxis1.setLabelCount(5, false);
        rightAxis1.setDrawGridLines(false);
        rightAxis1.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis2 = chart2.getAxisRight();
        rightAxis2.setTypeface(mTf);
        rightAxis2.setLabelCount(5, false);
        rightAxis2.setDrawGridLines(false);
        rightAxis2.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        chart1.animateX(750);
        chart2.animateX(750);
    }

    private void initTestData() {
        chart1.setData(generateDataLine(0));
        chart2.setData(generateDataLine(1));
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (chart1 != null) {
                chart1.animateY(700);
            }
            if (chart2 != null) {
                chart2.animateY(700);
            }
        }
    }

    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> values1 = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            values1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

        LineDataSet d1 = new LineDataSet(values1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> values2 = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            values2.add(new Entry(i, values1.get(i).getY() - 30));
        }

        LineDataSet d2 = new LineDataSet(values2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        return new LineData(sets);
    }


}
