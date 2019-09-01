package cn.dabin.opensource.ble.base;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import java.nio.charset.StandardCharsets;

import cn.dabin.opensource.ble.event.BleEvent;
import cn.dabin.opensource.ble.util.StringUtils;
import github.opensource.dialog.BeToastUtil;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.base
 * Created by :  dabin.
 * Created time: 2019/8/27 16:09
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:09
 * Class description:
 */
public abstract class BaseFragment extends Fragment {
    public View view;
    private boolean isLazyLoaded;
    private boolean isPrepared;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        lazyLoad();
    }


    /**
     * 获取状态栏高度
     */
    public int getStatusBarHeight() {
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return getResources().getDimensionPixelSize(resourceId);
        }
        return -1;
    }


    //显示信息toast
    protected void showCenterInfoMsg(String text) {
        if (!TextUtils.isEmpty(text)) {
            BeToastUtil.get().showCenterInfoMsg(getActivity(), text);
        }
    }


    //显示成功toast
    protected void showCenterSuccessMsg(String text) {
        if (!TextUtils.isEmpty(text)) {
            BeToastUtil.get().showCenterSuccessMsg(getActivity(), text);
        }
    }

    //显示错误snaker
    protected void showTopWrongMsg(String text) {
        if (!TextUtils.isEmpty(text)) {
            BeToastUtil.get().showTopWrongMsg(getActivity(), text);
        }
    }


    /**
     * 调用懒加载
     */

    private void lazyLoad() {
        if (getUserVisibleHint() && isPrepared && !isLazyLoaded) {
            onLazyLoad();
            isLazyLoaded = true;
        }
    }

    protected abstract int getLayoutId();

    @UiThread
    public abstract void onLazyLoad();


    @Override public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(getLayoutId(), container, false);
        } else {
            //  二次加载删除上一个子view
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(view);
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        //只有Fragment onCreateView好了，
        //另外这里调用一次lazyLoad(）
        lazyLoad();
    }


    public void sendMessage(String message) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        byte[] value;
        //send data to service
        value = message.getBytes(StandardCharsets.UTF_8);
        EventBus.getDefault().post(new BleEvent(BleEvent.ACTION_SEND_MSG, value));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}