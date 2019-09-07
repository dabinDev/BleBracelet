package cn.dabin.opensource.ble.ui.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import cn.dabin.opensource.ble.R;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.view
 * Created by :  dabin.
 * Created time: 2019/9/8 2:25
 * Changed by :  dabin.
 * Changed time: 2019/9/8 2:25
 * Class description:
 */
public class BasePopwindow extends PopupWindow {
    private Context mContext;

    public BasePopwindow(Context context) {
        // 设置布局的参数
        this(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public BasePopwindow(Context context, String temp) {
        // 设置布局的参数
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public BasePopwindow(Context context, int width, int height) {
        this.mContext = context;
        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        setAnimationStyle(R.style.AnimationPreview);
        // 设置弹窗外可点击
        setOutsideTouchable(false);
        // 设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);

    }

    public BasePopwindow(Context context, int width, int height, int anim) {
        this.mContext = context;
        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        if (anim != 0) {
            setAnimationStyle(anim);
        }
        else {

        }
        // 设置弹窗外可点击
        setOutsideTouchable(false);
        // 设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        params.alpha = 0.5f;
        ((Activity) mContext).getWindow().setAttributes(params);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        params.alpha = 0.5f;
        ((Activity) mContext).getWindow().setAttributes(params);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        params.alpha = 0.5f;
        ((Activity) mContext).getWindow().setAttributes(params);
    }


    public void showCenter(Activity activity)
    {
        showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public void showFrromBottom(View parent) {
        super.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }


    public void showAsDropDownWhite(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        params.gravity = Gravity.END | Gravity.TOP;
        ((Activity) mContext).getWindow().setAttributes(params);
    }

    public void initUI(View view) {
        if (view != null) {
            setContentView(view);
        }

    }


    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        params.alpha = 1f;
        ((Activity) mContext).getWindow().setAttributes(params);
    }
}
