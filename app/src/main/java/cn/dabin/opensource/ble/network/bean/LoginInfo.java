package cn.dabin.opensource.ble.network.bean;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.network.bean
 * Created by :  dabin.
 * Created time: 2019/8/31 23:18
 * Changed by :  dabin.
 * Changed time: 2019/8/31 23:18
 * Class description:
 */
public class LoginInfo {
    private String mobile;
    private String code;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
