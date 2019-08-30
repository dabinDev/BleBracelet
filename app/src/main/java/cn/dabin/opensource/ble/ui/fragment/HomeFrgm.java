package cn.dabin.opensource.ble.ui.fragment;

import android.content.Context;

import com.gyf.immersionbar.ImmersionBar;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseFragment;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.fragment
 * Created by :  dabin.
 * Created time: 2019/8/27 16:26
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:26
 * Class description:
 */
public class HomeFrgm extends BaseFragment {
    @Override protected int getLayoutId() {
        return R.layout.frgm_home;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
    }



    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ImmersionBar.with(this)
                    .statusBarView(R.id.topPanel)
                    .statusBarColor(R.color.colorStatusBarBlue)
                    .statusBarDarkFont(false)
                    .fullScreen(true)
                    .init();
        }
    }

    @Override public void onLazyLoad() {

    }
}
