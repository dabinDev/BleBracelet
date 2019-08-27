package cn.dabin.opensource.ble.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.dabin.opensource.ble.util.AppManager;
import github.opensource.dialog.BeToastUtil;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.base
 * Created by :  dabin.
 * Created time: 2019/8/27 16:11
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:11
 * Class description:
 */
public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected Resources mResource;
    protected boolean showInput = true;
    private Intent serviceIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 友盟推送
        mContext = this;
        mResource = getResources();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    //显示信息toast
    public void showCenterInfoMsg(String text) {
        if (!TextUtils.isEmpty(text)) {
            BeToastUtil.get().showCenterInfoMsg(getApplicationContext(), text);
        }
    }


    //显示成功toast
    protected void showCenterSuccessMsg(String text) {
        if (!TextUtils.isEmpty(text)) {
            BeToastUtil.get().showCenterSuccessMsg(getApplicationContext(), text);
        }
    }

    //显示错误snaker
    public void showTopWrongMsg(String text) {
        if (!TextUtils.isEmpty(text)) {
            BeToastUtil.get().showTopWrongMsg(getApplicationContext(), text);
        }
    }


    // 输入框的控制
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev) && showInput) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
        AppManager.getAppManager().finishActivity(this);
    }
}

