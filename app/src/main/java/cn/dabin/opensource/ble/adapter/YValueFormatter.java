package cn.dabin.opensource.ble.adapter;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

import cn.dabin.opensource.ble.util.StringUtils;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.adapter
 * Created by :  dabin.
 * Created time: 2019/9/9 22:19
 * Changed by :  dabin.
 * Changed time: 2019/9/9 22:19
 * Class description:
 */
public class YValueFormatter extends ValueFormatter {
    private ArrayList<String> list;

    public YValueFormatter(ArrayList<String> list) {
        this.list = list;
    }

    @Override public String getFormattedValue(float value) {
        return StringUtils.value((int)value);
    }
}
