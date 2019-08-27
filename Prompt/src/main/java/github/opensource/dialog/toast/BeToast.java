package github.opensource.dialog.toast;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import github.opensource.dialog.R;


/**
 * Project :  BePrompt.
 * Package name: github.opensource.benjamin.beprompt.toast
 * Created by :  Benjamin.
 * Created time: 2017/12/7 15:28
 * Changed by :  Benjamin.
 * Changed time: 2017/12/7 15:28
 * Class description:
 */

public class BeToast {
    private static BeToast beToast = new BeToast();
    private static Toast toast;

    private BeToast() {

    }

    public static BeToast get() {
        return beToast;
    }

    public void showTopWrongMsg(Context context, int img, String text) {
        String threadName=Thread.currentThread().getName();
        Log.v("当前线程" + threadName,"");


        if (toast == null) {
            toast = new Toast(context);
        }
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_betoast_top_message, null);
        TextView tv_message = layout.findViewById(R.id.betoast_message);
        ImageView iv_icon = layout.findViewById(R.id.betoast_icon);
        if (img != 0) {
            iv_icon.setImageResource(img);
        }
        if (text != null && text.length() > 0) {
            tv_message.setText(text);
        }
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    public void showCenterInfoMsg(Context context, String text) {
        String threadName=Thread.currentThread().getName();
        Log.v("当前线程" + threadName,"");

        if (toast == null) {
            toast = new Toast(context);
        }
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_betoast_center_toast, null);
        TextView tv_message = layout.findViewById(R.id.betoast_message);
        if (text != null && text.length() > 0) {
            tv_message.setText(text);
        }
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public synchronized void showCenterSuccessMsg(Context context, int img, String text) {
        String threadName=Thread.currentThread().getName();
        Log.v("当前线程" + threadName,"");

        if (toast == null) {
            toast = new Toast(context);
        }
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_betoast_center_message, null);
        TextView tv_message = layout.findViewById(R.id.betoast_message);
        ImageView iv_icon = layout.findViewById(R.id.betoast_icon);
        if (img != 0) {
            iv_icon.setImageResource(img);
        }
        if (text != null && text.length() > 0) {
            tv_message.setText(text);
        }
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
