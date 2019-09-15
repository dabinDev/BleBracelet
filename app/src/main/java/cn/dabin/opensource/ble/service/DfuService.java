package cn.dabin.opensource.ble.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.ui.activity.HomeAct;
import no.nordicsemi.android.dfu.BuildConfig;
import no.nordicsemi.android.dfu.DfuBaseService;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.service
 * Created by :  dabin.
 * Created time: 2019/9/6 11:04
 * Changed by :  dabin.
 * Changed time: 2019/9/6 11:04
 * Class description:
 */
public class DfuService extends DfuBaseService {
    private String notificationId = "1";
    private String notificationName = "迈宝";

    @Override public void onCreate() {
        super.onCreate();
        //创建NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(1, getNotification());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Nullable @Override protected Class<? extends Activity> getNotificationTarget() {
        return HomeAct.class;
    }

    private Notification getNotification() {
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= 26) {
            builder = new NotificationCompat.Builder(getApplicationContext(), notificationId);

        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("dfu服务")
                .setTicker("dfu更新服务")//通知首次出现在通知栏，带上升动画效果的  
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间  
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级  
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消    
                .setOngoing(false)//true，设置他为一个正在进行的通知  
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单
                .setContentText("正在进行dfu升级");
        //设置Notification的ChannelID,否则不能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationId);
        }
        return builder.build();
    }


    @Override public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        } else {
            stopForeground(true);
        }
    }

    @Override
    protected boolean isDebug() {
        // Here return true if you want the service to print more logs in LogCat.
        // Library's BuildConfig in current version of Android Studio is always set to DEBUG=false, so
        // make sure you return true or your.app.BuildConfig.DEBUG here.
        return BuildConfig.DEBUG;
    }

}
