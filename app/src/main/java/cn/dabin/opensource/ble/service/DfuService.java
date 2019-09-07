package cn.dabin.opensource.ble.service;

import android.app.Activity;

import androidx.annotation.Nullable;

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
    @Nullable @Override protected Class<? extends Activity> getNotificationTarget() {
        return null;
    }
}
