package cn.dabin.opensource.ble.network.callback;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.request.base.Request;

import cn.dabin.opensource.ble.util.Logger;
import okhttp3.Response;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.network.callback
 * Created by :  dabin.
 * Created time: 2019/8/31 2:25
 * Changed by :  dabin.
 * Changed time: 2019/8/31 2:25
 * Class description:
 */
public abstract class MineCallBack extends AbsCallback<String> {
    private StringConvert convert;

    public MineCallBack() {
        convert = new StringConvert();
    }

    @Override public void onStart(Request<String, ? extends Request> request) {
        request.headers("123", "123");
        super.onStart(request);
    }

    @Override
    public String convertResponse(Response response) throws Throwable {
        String s = convert.convertResponse(response);
        response.close();
        Logger.d(this.getClass().getSimpleName(), response.body().toString());
        return s;
    }
}
