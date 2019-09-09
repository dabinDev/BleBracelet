package cn.dabin.opensource.ble.network.bean;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.network.bean
 * Created by :  dabin.
 * Created time: 2019/9/9 14:58
 * Changed by :  dabin.
 * Changed time: 2019/9/9 14:58
 * Class description:
 */
public class ResultMsg {
    private boolean success;
    private String message;
    private String encoding;
    private Object model;
    private String code;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
