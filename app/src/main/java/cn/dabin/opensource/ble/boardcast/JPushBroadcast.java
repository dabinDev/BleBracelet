package cn.dabin.opensource.ble.boardcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.dabin.opensource.ble.util.Logger;

import static cn.dabin.opensource.ble.ui.activity.HomeAct.KEY_EXTRAS;
import static cn.dabin.opensource.ble.ui.activity.HomeAct.KEY_MESSAGE;
import static cn.dabin.opensource.ble.ui.activity.HomeAct.MESSAGE_RECEIVED_ACTION;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.boardcast
 * Created by :  dabin.
 * Created time: 2019/8/30 17:37
 * Changed by :  dabin.
 * Changed time: 2019/8/30 17:37
 * Class description:
 */
public class JPushBroadcast extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        try {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                Logger.d(JPushBroadcast.class.getName(), showMsg.toString());
            }
        } catch (Exception e) {
        }
    }
}
