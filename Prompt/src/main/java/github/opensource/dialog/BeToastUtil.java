package github.opensource.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import github.opensource.dialog.prompt.PromptDialog;
import github.opensource.dialog.sneaker.Sneaker;
import github.opensource.dialog.toast.BeToast;
import github.opensource.dialog.toast.TastyToast;

/**
 * Project : Promptlibrary.
 * Package name: github.opensource.dialog
 * Created by :  Benjamin.
 * Created time: 2017/10/30 15:35
 * Changed by :  Benjamin.
 * Changed time: 2017/10/30 15:35
 * Class description:
 */

public class BeToastUtil {
    static final BeToastUtil utils = new BeToastUtil();
    private PromptDialog promptDialog;


    public synchronized static BeToastUtil get() {
        return utils;
    }


    /**
     * 显示中部成功消息
     * @param context
     * @param text
     */
    public synchronized void showCenterSuccessMsg(Context context, String text) {
        // TODO 17/12/06
        if (context == null || text == null || text.length() == 0) {
            return;
        }
        BeToast.get().showCenterSuccessMsg(context, R.mipmap.ic_done, text);

       /* BeCenterMessage.Builder center = new BeCenterMessage.Builder(context);
        center.setDuringTime(1000)
                .setGravity(Gravity.CENTER)
                .setMessage(text)
                .setIcon(R.mipmap.ic_done)
                .show();*/
    }

    /**
     * 显示中部消息
     * @param context
     * @param text
     */
    public synchronized void showCenterInfoMsg(Context context, String text) {
        if (context == null || text == null || text.length() == 0) {
            return;
        }

        BeToast.get().showCenterInfoMsg(context, text);

       /* BeCenterToast.Builder TOAST = new BeCenterToast.Builder(context);
        TOAST.setDuringTime(1000)
                .setGravity(Gravity.CENTER)
                .setMessage(text)
                .show();*/
    }


    /**
     * 显示顶部错误消息
     * @param context
     * @param text
     */
    public synchronized void showTopWrongMsg(Context context, String text) {
        if (context == null || text == null || text.length() == 0) {
            return;
        }

        BeToast.get().showTopWrongMsg(context, R.mipmap.ic_wrong, text);

      /*  BeTopMessage.Builder builder = new BeTopMessage.Builder(context);
        builder.setDuringTime(2000)
                .setGravity(Gravity.TOP)
                .setMessage(text)
                .setIcon(R.mipmap.ic_wrong)
                .show();*/
    }
    public synchronized void showErrorSnaker(Context a, String text) {
        Sneaker.with((Activity) a)
                .setTitle("系统提示", R.color.color_white) // Title and title color
                .setMessage(text, R.color.color_white) // Message and message color
                .setDuration(4000) // Time duration to show
                .setIcon(R.drawable.ic_error, R.color.color_gray, false)
                .autoHide(true) // Auto hide Sneaker view
                .sneak(R.color.color_blue);
    }

    public synchronized void showSuccessSnaker(Context a, String text) {
        Sneaker.with((Activity) a)
                .setTitle("系统提示", R.color.color_white) // Title and title color
                .setMessage(text, R.color.color_white) // Message and message color
                .setDuration(4000) // Time duration to show
                .setIcon(R.drawable.ic_success, R.color.color_green, false)
                .autoHide(true) // Auto hide Sneaker view
                .sneak(R.color.color_blue);
    }

    public synchronized void showInfoSnaker(Context a, String text) {
        if (!TextUtils.isEmpty(text))
            Sneaker.with((Activity) a)
                    .setTitle("系统提示", R.color.color_white) // Title and title color
                    .setMessage(text, R.color.color_white) // Message and message color
                    .setDuration(4000) // Time duration to show
                    .setIcon(R.drawable.ic_warning, R.color.color_red, false)
                    .autoHide(true) // Auto hide Sneaker view
                    .sneak(R.color.color_blue);
    }

    public synchronized void showDownToast(Context a, String text) {
        TastyToast.makeText(a, text, TastyToast.LENGTH_SHORT,
                TastyToast.SUCCESS);
    }

    public synchronized void showWaringToast(Context a, String text) {
        TastyToast.makeText(a, text, TastyToast.LENGTH_SHORT,
                TastyToast.WARNING);
    }

    public synchronized void showErrorToast(Context a, String text) {
        TastyToast.makeText(a, text, TastyToast.LENGTH_SHORT,
                TastyToast.ERROR);
    }

    public synchronized void showInfoToast(Context a, String text) {
        try {
            TastyToast.makeText(a, text, TastyToast.LENGTH_SHORT,
                    TastyToast.INFO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void showDefaultToast(Context a, String text) {
        TastyToast.makeText(a, text, TastyToast.LENGTH_SHORT,
                TastyToast.DEFAULT);
    }

    public synchronized void showConfusionToast(Context a, String text) {
        TastyToast.makeText(a, text, TastyToast.LENGTH_SHORT,
                TastyToast.CONFUSING);
    }


    public synchronized void showDownToastL(Context a, String text) {
        TastyToast.makeText(a, text, TastyToast.LENGTH_LONG,
                TastyToast.SUCCESS);
    }

    public synchronized void showWaringToastL(Context a, String text) {
        TastyToast.makeText(a, text, TastyToast.LENGTH_LONG,
                TastyToast.WARNING);
    }

    public synchronized void showErrorToastL(Context a, String text) {
        TastyToast.makeText(a, text, TastyToast.LENGTH_LONG,
                TastyToast.ERROR);
    }

    public synchronized void showInfoToastL(Context a, String text) {
        try {
            TastyToast.makeText(a, text, TastyToast.LENGTH_LONG,
                    TastyToast.INFO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void showDefaultToastl(Context a, String text) {
        TastyToast.makeText(a, text, TastyToast.LENGTH_LONG,
                TastyToast.DEFAULT);
    }

    public synchronized void showConfusionToastL(Context a, String text) {
        TastyToast.makeText(a, text, TastyToast.LENGTH_LONG,
                TastyToast.CONFUSING);
    }

}
