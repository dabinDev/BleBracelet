
package cn.dabin.opensource.ble.network.bean;

import com.google.gson.annotations.Expose;


public class BabyInfoBean {

    @Expose
    private int code;
    @Expose
    private String encoding;
    @Expose
    private String message;
    @Expose
    private BabyInfo model;
    @Expose
    private Boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BabyInfo getModel() {
        return model;
    }

    public void setModel(BabyInfo model) {
        this.model = model;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
