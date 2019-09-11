package cn.dabin.opensource.ble.adapter;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.adapter
 * Created by :  dabin.
 * Created time: 2019/9/9 23:21
 * Changed by :  dabin.
 * Changed time: 2019/9/9 23:21
 * Class description:
 */
public class YValueBarDataSet extends BarDataSet {

    public YValueBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override public int getColor(int index) {
        float distance = getEntryForIndex(index).getY();
        if (distance < 100) {
            return mColors.get(0);
        } else if (distance > 100 && distance < 320) {
            return mColors.get(1);
        } else if (distance > 320 && distance < 500) {
            return mColors.get(2);
        } else if (distance > 500 && distance < 8191) {
            return mColors.get(3);
        } else {
            return mColors.get(4);
        }

    }

}
