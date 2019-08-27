package github.benjamin.bottombar;

import androidx.viewpager.widget.ViewPager;

/**
 * Project :  BottomBar.
 * Package name: github.benjamin.bottombar
 * Created by :  Benjamin.
 * Created time: 2018/1/5 13:38
 * Changed by :  Benjamin.
 * Changed time: 2018/1/5 13:38
 * Class description:
 */

interface BottomLayoutController {
    /**
     * 方便适配ViewPager页面切换<p>
     * 注意：ViewPager页面数量必须等于导航栏的Item数量
     * @param viewPager {@link ViewPager}
     */
    void setupWithViewPager(ViewPager viewPager);

    /**
     * 向下移动隐藏导航栏
     */
    void hideBottomLayout();

    /**
     * 向上移动显示导航栏
     */
    void showBottomLayout();
}
