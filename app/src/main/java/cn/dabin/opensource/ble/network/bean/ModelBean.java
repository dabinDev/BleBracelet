
package cn.dabin.opensource.ble.network.bean;

import com.google.gson.annotations.Expose;


public class ModelBean {

    @Expose
    private Object code;
    @Expose
    private String encoding;
    @Expose
    private String message;
    @Expose
    private String model;
    @Expose
    private Boolean success;

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
