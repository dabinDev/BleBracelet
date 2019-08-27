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
 * Created by :  Benjamin.B
 * Created time: 2017/12/6 12:44
 * Changed by :  Benjamin.
 * Changed time: 2017/12/6 12:44
 * Class description:
 */

public class BeCenterMessage extends Dialog {

    public BeCenterMessage(@NonNull Context context) {
        super(context);
    }

    public BeCenterMessage(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

    public static class Builder {
        private Context context;
        private String message;
        private int duringTime;
        private TextView tv_message;
        private ImageView iv_icon;
        private int gravity;
        private int icon;

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

        public Builder setIcon(int icon) {
            this.icon = icon;
            return this;
        }

        public void show() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final BeCenterMessage dialog = new BeCenterMessage(context, R.style.prompt_style);
            View layout = inflater.inflate(R.layout.layout_betoast_center_message, null);
            tv_message = layout.findViewById(R.id.betoast_message);
            iv_icon = layout.findViewById(R.id.betoast_icon);
            if (tv_message != null) {
                tv_message.setText(message);
            }
            if (icon != 0) {
                iv_icon.setImageResource(icon);
            }
            else {
                iv_icon.setVisibility(View.GONE);
            }
            // 设置对话框的视图
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(layout, layoutParams);
            Window window = dialog.getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.pop_animation_top);
            }
            if (window != null) {
                window.setGravity(gravity);
            }
            if (Build.VERSION.SDK_INT > 24) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
            }
            else if (Build.VERSION.SDK_INT == 24) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            else {

            }
            dialog.show();
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
