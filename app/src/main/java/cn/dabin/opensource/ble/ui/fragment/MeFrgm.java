package cn.dabin.opensource.ble.ui.fragment;

import com.gyf.immersionbar.ImmersionBar;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseFragment;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.fragment
 * Created by :  dabin.
 * Created time: 2019/8/27 16:59
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:59
 * Class description:
 */
public class MeFrgm extends BaseFragment {
    @Override protected int getLayoutId() {
        return R.layout.frgm_me;
    }

    @Override public void onLazyLoad() {

    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (this.getActivity()==null)
            {
                return;
            }
            ImmersionBar.with(this)
                    .statusBarColor(R.color.color_white)
                    .statusBarDarkFont(true)
                    .fullScreen(true)
                    .init();
        }
    }

}
