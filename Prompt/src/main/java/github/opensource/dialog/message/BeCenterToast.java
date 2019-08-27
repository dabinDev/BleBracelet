package github.opensource.dialog.message;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import github.opensource.dialog.R;

/**
 * Project :  BePrompt.
 * Package name: github.opensource.benjamin.beprompt
 * Created by :  Benjamin.
 * Created time: 2017/12/6 12:44
 * Changed by :  Benjamin.
 * Changed time: 2017/12/6 12:44
 * Class description:
 */

public class BeCenterToast extends Dialog {

    public BeCenterToast(@NonNull Context context) {
        super(context);
    }

    public BeCenterToast(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String message;
        private int duringTime;
        private TextView tv_message;
        private ImageView iv_icon;
        private int gravity;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        //用于暴露出来设置文字样式
        public TextView getMessageView() {
            return tv_message;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setDuringTime(int duringTime) {
            this.duringTime = duringTime;
            return this;
        }


        public void show() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final BeCenterToast dialog = new BeCenterToast(context, R.style.toast_style);
            View layout = inflater.inflate(R.layout.layout_betoast_center_toast, null);
            tv_message = layout.findViewById(R.id.betoast_message);
            iv_icon = layout.findViewById(R.id.betoast_icon);

            if (tv_message != null) {
                tv_message.setText(message);
            }

            // 设置对话框的视图
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(layout, params);
            Window window = dialog.getWindow();
            if (window != null) {
                window.setGravity(gravity);
            }
            if (window != null) {
                window.setWindowAnimations(R.style.pop_animation_top);
            }
            //
            /**
             * alert弹窗
             * TYPE_TOAST   Android7.1.1 手机上会出现崩溃
             * TYPE_SYSTEM_ALERT  在vivo手机上会出现崩溃
             */
            int verssionCode = Build.VERSION.SDK_INT;
            if (Build.VERSION.SDK_INT > 24) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
            }
            else if (Build.VERSION.SDK_INT <= 24) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            if (dialog != null) {
                dialog.show();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, duringTime);
        }
    }
}
