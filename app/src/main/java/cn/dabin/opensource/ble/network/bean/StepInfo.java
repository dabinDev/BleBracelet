package cn.dabin.opensource.ble.network.bean;

import cn.dabin.opensource.ble.util.DateUtil;
import cn.dabin.opensource.ble.util.StringUtils;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.network.bean
 * Created by :  dabin.
 * Created time: 2019/9/8 22:32
 * Changed by :  dabin.
 * Changed time: 2019/9/8 22:32
 * Class description:
 */
public class StepInfo {
    private long time;
    private int step;

    public StepInfo(String stepInfo) {
        if (StringUtils.isNotEmpty(stepInfo) && stepInfo.contains("t:") && stepInfo.contains("b:")) {
            long time = Long.valueOf(stepInfo.substring(stepInfo.indexOf("t") + 2, stepInfo.indexOf("b")).trim());
            int step = Integer.valueOf(stepInfo.substring(stepInfo.indexOf("b") + 2).trim());
            setStep(step);
            setTime(DateUtil.removeSecond(time));
        }
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }



}
