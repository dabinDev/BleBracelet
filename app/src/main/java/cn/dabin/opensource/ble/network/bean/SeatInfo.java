package cn.dabin.opensource.ble.network.bean;

import cn.dabin.opensource.ble.util.DateUtil;
import cn.dabin.opensource.ble.util.StringUtils;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.network.bean
 * Created by :  dabin.
 * Created time: 2019/9/11 18:49
 * Changed by :  dabin.
 * Changed time: 2019/9/11 18:49
 * Class description:
 */
public class SeatInfo {
    private long time;

    public SeatInfo(String stepInfo) {
        if (StringUtils.isNotEmpty(stepInfo) && stepInfo.contains("l:")) {
            long time = Long.valueOf(stepInfo.replaceAll("l:", "").trim());
            setTime(DateUtil.removeSecond(time));
        }
    }

    public void setTime(long time) {
        this.time = time;
    }
}
