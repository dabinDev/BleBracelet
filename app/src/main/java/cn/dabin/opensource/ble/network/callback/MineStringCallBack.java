package cn.dabin.opensource.ble.network.callback;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.request.base.Request;
import com.lzy.okgo.utils.OkLogger;

import java.util.Iterator;
import java.util.LinkedHashMap;

import cn.dabin.opensource.ble.global.BleApplication;
import github.opensource.dialog.BeToastUtil;
import okhttp3.Response;

public abstract class MineStringCallback extends AbsCallback<String> {

    private StringConvert convert;

    public MineStringCallback() {
        convert = new StringConvert();
    }


    public abstract void success(String result);

    @Override
    public String convertResponse(Response response) throws Throwable {
        String s = convert.convertResponse(response);
        response.close();
        return s;
    }

    @Override public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        OkLogger.d("onStart--------------------------------------");
        OkLogger.d("url-----------  " + request.getUrl());
        Iterator iter = request.getParams().urlParamsMap.entrySet().iterator();
        while (iter.hasNext()) {
            LinkedHashMap.Entry entry = (LinkedHashMap.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            OkLogger.d("参数---:\r\n [key]:" + key.toString() + "\r\n[value]:" + val.toString());
        }
        OkLogger.d("onStart----------------end-------------------");


        if (request.getRequest() != null && request.getRequest().body() != null) {
            OkLogger.d("onStart--------------------------------------");
            String reqBody = request.getRequest().body().toString();
            OkLogger.d("request body-----------  " + reqBody);
            OkLogger.d("onStart----------------end-------------------");
        }

    }

    @Override public void onSuccess(com.lzy.okgo.model.Response<String> response) {
        OkLogger.d("onSuccess--------------------------------------");
        OkLogger.d(response.body());
        OkLogger.d("onSuccess----------------end-------------------");
        success(response.body());
    }


    @Override
    public void onError(com.lzy.okgo.model.Response<String> response) {
        OkLogger.d("onError--------------------------------------");
        OkLogger.printStackTrace(response.getException());
        BeToastUtil.get().showTopWrongMsg(BleApplication.getApplication(), "网络异常");
        OkLogger.d("onError----------------end-------------------");
    }

}
