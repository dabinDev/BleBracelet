package github.opensource.dialog.toast;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import github.opensource.dialog.R;


/**
 * Project :  BeToast.
 * Package name:github.opensource.dialog.toast
 * Created by :  Benjamin.
 * Created time: 2017/10/30 15:35
 * Changed by :  Benjamin.
 * Changed time: 2017/10/30 15:35
 * Class description:
 */
public class TastyToast {
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;
    public static final int SUCCESS = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;
    public static final int INFO = 4;
    public static final int DEFAULT = 5;
    public static final int CONFUSING = 6;
    static SuccessToastView successToastView;
    static WarningToastView warningToastView;
    static ErrorToastView errorToastView;
    static InfoToastView infoToastView;
    static DefaultToastView defaultToastView;
    static ConfusingToastView confusingToastView;
    private static String oldMsg;
    private static long time;

    public static synchronized Toast makeText(Context context, String msg, int length, int type) {
        oldMsg = msg;
        Toast toast = new Toast(context);
        switch (type) {
            case 1: {
                View layout = LayoutInflater.from(context).inflate(R.layout.success_toast_layout, null, false);
                TextView text = layout.findViewById(R.id.toastMessage);
                text.setText(msg);
                successToastView = layout.findViewById(R.id.successView);
                successToastView.startAnim();
                text.setBackgroundResource(R.drawable.success_toast);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.setView(layout);
                break;
            }
            case 2: {
                View layout = LayoutInflater.from(context).inflate(R.layout.warning_toast_layout, null, false);
                TextView text = layout.findViewById(R.id.toastMessage);
                text.setText(msg);

                warningToastView = layout.findViewById(R.id.warningView);
                SpringSystem springSystem = SpringSystem.create();
                final Spring spring = springSystem.createSpring();
                spring.setCurrentValue(1.8);
                SpringConfig config = new SpringConfig(40, 5);
                spring.setSpringConfig(config);
                spring.addListener(new SimpleSpringListener() {

                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float value = (float) spring.getCurrentValue();
                        float scale = 0.9f - (value * 0.5f);

                        warningToastView.setScaleX(scale);
                        warningToastView.setScaleY(scale);
                    }
                });
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }
                        spring.setEndValue(0.4f);
                    }
                });

                t.start();
                text.setBackgroundResource(R.drawable.warning_toast);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.setView(layout);
                break;
            }
            case 3: {
                View layout = LayoutInflater.from(context).inflate(R.layout.error_toast_layout, null, false);

                TextView text = layout.findViewById(R.id.toastMessage);
                text.setText(msg);
                errorToastView = layout.findViewById(R.id.errorView);
                errorToastView.startAnim();
                text.setBackgroundResource(R.drawable.error_toast);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.setView(layout);
                break;
            }
            case 4: {
                View layout = LayoutInflater.from(context).inflate(R.layout.info_toast_layout, null, false);

                TextView text = layout.findViewById(R.id.toastMessage);
                text.setText(msg);
                infoToastView = layout.findViewById(R.id.infoView);
                infoToastView.startAnim();
                text.setBackgroundResource(R.drawable.info_toast);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.setView(layout);
                break;
            }
            case 5: {
                View layout = LayoutInflater.from(context).inflate(R.layout.default_toast_layout, null, false);

                TextView text = layout.findViewById(R.id.toastMessage);
                text.setText(msg);
                defaultToastView = layout.findViewById(R.id.defaultView);
                defaultToastView.startAnim();
                text.setBackgroundResource(R.drawable.default_toast);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.setView(layout);
                break;
            }
            case 6: {
                View layout = LayoutInflater.from(context).inflate(R.layout.confusing_toast_layout, null, false);
                TextView text = layout.findViewById(R.id.toastMessage);
                text.setText(msg);
                confusingToastView = layout.findViewById(R.id.confusingView);
                confusingToastView.startAnim();
                text.setBackgroundResource(R.drawable.confusing_toast);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.setView(layout);
                break;
            }
            default:
                break;
        }
        if (isForeground(context)) {//判斷app在前胎的時候才可以彈窗
            if (!msg.equals(oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
                time = System.currentTimeMillis();
                toast.setDuration(length);
                toast.show();
            }
            else {
                // 显示内容一样时，只有间隔时间大于2秒时才显示
                if (System.currentTimeMillis() - time > 2000) {
                    time = System.currentTimeMillis();
                    toast.setDuration(length);
                    toast.show();
                }
            }
        }
        oldMsg = msg;
        return toast;
    }

    private static boolean isForeground(Context context) {
        if (context != null) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            String currentPackageName = cn.getPackageName();
            return !TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName());
        }
        return false;
    }

}
