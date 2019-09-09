package cn.dabin.opensource.ble.adapter;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.adapter
 * Created by :  dabin.
 * Created time: 2019/9/9 22:19
 * Changed by :  dabin.
 * Changed time: 2019/9/9 22:19
 * Class description:
 */
public class XValueFormatter extends ValueFormatter {
    private ArrayList<String> list;

    public XValueFormatter(ArrayList<String> list) {
        this.list = list;
    }

    @Override public String getFormattedValue(float value) {
        //LogUtils.e(value+"");
        int values = (int) value;
        if(values<0){
            values = 0;
        }
        if(values>=list.size()){
            values = list.size()-1;
        }
        return list.get(values%list.size());
    }
}
