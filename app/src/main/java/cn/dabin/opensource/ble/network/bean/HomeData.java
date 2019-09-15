package cn.dabin.opensource.ble.network.bean;

import com.google.gson.annotations.Expose;


public class HomeData {


    @Expose
    private String baifenbi;
    @Expose
    private String cuowuyongyanshichang;
    @Expose
    private String dianlinag;
    @Expose
    private String jinjuliyanshichang;
    @Expose
    private String jiuzuo; //久坐数据
    @Expose
    private String step; //步数信息
    @Expose
    private String tongbuTime;//同步时间
    @Expose
    private String yigongfenzhong; //全部时间
    @Expose
    private String yongyanpingjinjuli;//用眼平均距离
    @Expose
    private String zhi;
    @Expose
    private String zuijincuowujuli;

    public String getBaifenbi() {
        return baifenbi;
    }

    public void setBaifenbi(String baifenbi) {
        this.baifenbi = baifenbi;
    }

    public String getCuowuyongyanshichang() {
        return cuowuyongyanshichang;
    }

    public void setCuowuyongyanshichang(String cuowuyongyanshichang) {
        this.cuowuyongyanshichang = cuowuyongyanshichang;
    }

    public String getDianlinag() {
        return dianlinag;
    }

    public void setDianlinag(String dianlinag) {
        this.dianlinag = dianlinag;
    }

    public String getJinjuliyanshichang() {
        return jinjuliyanshichang;
    }

    public void setJinjuliyanshichang(String jinjuliyanshichang) {
        this.jinjuliyanshichang = jinjuliyanshichang;
    }

    public String getJiuzuo() {
        return jiuzuo;
    }

    public void setJiuzuo(String jiuzuo) {
        this.jiuzuo = jiuzuo;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getTongbuTime() {
        return tongbuTime;
    }

    public void setTongbuTime(String tongbuTime) {
        this.tongbuTime = tongbuTime;
    }

    public String getYigongfenzhong() {
        return yigongfenzhong;
    }

    public void setYigongfenzhong(String yigongfenzhong) {
        this.yigongfenzhong = yigongfenzhong;
    }

    public String getYongyanpingjinjuli() {
        return yongyanpingjinjuli;
    }

    public void setYongyanpingjinjuli(String yongyanpingjinjuli) {
        this.yongyanpingjinjuli = yongyanpingjinjuli;
    }

    public String getZhi() {
        return zhi;
    }

    public void setZhi(String zhi) {
        this.zhi = zhi;
    }

    public String getZuijincuowujuli() {
        return zuijincuowujuli;
    }

    public void setZuijincuowujuli(String zuijincuowujuli) {
        this.zuijincuowujuli = zuijincuowujuli;
    }

}
