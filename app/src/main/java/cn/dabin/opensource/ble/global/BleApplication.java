package cn.dabin.opensource.ble.global;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.global
 * Created by :  dabin.
 * Created time: 2019/8/27 14:56
 * Changed by :  dabin.
 * Changed time: 2019/8/27 14:56
 * Class description:
 */
public class BleApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

    }
}
