package cn.dabin.opensource.ble.global;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.vise.baseble.ViseBle;

import org.litepal.LitePal;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import cn.dabin.opensource.ble.network.bean.BleInfo;
import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

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
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        LitePal.initialize(this);
        initOkGo();
        initBluetooth();

    }



    public static BleInfo getBleInfo(String macAddress)
    {
        return LitePal.where("macAddress=?",macAddress).findFirst(BleInfo.class);
    }


    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.ALL);
        builder.addInterceptor(loggingInterceptor);

        //全局的读取超时时间  基于前面的通道建立完成后，客户端终于可以向服务端发送数据了
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间  服务器发回消息，可是客户端出问题接受不到了
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间  http建立通道的时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));

        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                            //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
    }
    private void initBluetooth() {
        //蓝牙相关配置修改
        ViseBle.config()
                .setScanTimeout(5 * 1000)//扫描超时时间，这里设置为永久扫描
                .setConnectTimeout(10 * 1000)//连接超时时间
                .setOperateTimeout(5 * 1000)//设置数据操作超时时间
                .setConnectRetryCount(3)//设置连接失败重试次数
                .setConnectRetryInterval(1000)//设置连接失败重试间隔时间
                .setOperateRetryCount(3)//设置数据操作失败重试次数
                .setOperateRetryInterval(1000)//设置数据操作失败重试间隔时间
                .setMaxConnectCount(1);//设置最大连接设备数量
//蓝牙信息初始化，全局唯一，必须在应用初始化时调用
        ViseBle.getInstance().init(this);
    }

}
