package cn.dabin.opensource.ble.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.adapter.ViewpagerFragmentAdapter;
import cn.dabin.opensource.ble.base.BaseActivity;
import cn.dabin.opensource.ble.ui.fragment.DataFagm;
import cn.dabin.opensource.ble.ui.fragment.GuardianFagm;
import cn.dabin.opensource.ble.ui.fragment.HomeFrgm;
import cn.dabin.opensource.ble.ui.fragment.MeFrgm;
import cn.dabin.opensource.ble.ui.fragment.SettingFrgm;
import github.benjamin.bottombar.NavigationController;
import github.benjamin.bottombar.PageNavigationView;
import github.benjamin.bottombar.item.BaseTabItem;
import github.benjamin.bottombar.tabs.SpecialTab;
import github.benjamin.bottombar.tabs.SpecialTabRound;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble
 * Created by :  dabin.
 * Created time: 2019/8/27 14:28
 * Changed by :  dabin.
 * Changed time: 2019/8/27 14:28
 * Class description:
 */
public class HomeAct extends BaseActivity {
    public static final String MESSAGE_RECEIVED_ACTION = "cn.dabin.opensource.ble.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;
    private ViewPager viewPager;
    private BaseTabItem tabsMessage;
    private BaseTabItem tabsContact;
    private BaseTabItem tabsWork;
    private BaseTabItem tabsChart;
    private BaseTabItem tabsMine;
    private FrameLayout mainContent;

    public static void openAct(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, HomeAct.class);
        context.startActivity(intent);
    }


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        initView();
    }

    private void initView() {
        mainContent = findViewById(R.id.main_content);
        viewPager = findViewById(R.id.viewPager);
        initBottomBar();
    }




    private void initBottomBar() {
        PageNavigationView tab = findViewById(R.id.tab);
        tabsMessage = newItem(R.mipmap.ic_message_def, R.mipmap.ic_message_sel, "首页");
        tabsContact = newItem(R.mipmap.ic_contact_def, R.mipmap.ic_contact_sel, "监控");
        tabsWork = newRoundItem(R.mipmap.ic_center_def, R.mipmap.ic_center_sel, "数据");
        tabsChart = newItem(R.mipmap.ic_chart_def, R.mipmap.ic_chart_sel, "设置");
        tabsMine = newItem(R.mipmap.ic_mine_def, R.mipmap.ic_mine_sel, "我的");

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFrgm());
        fragmentList.add(new GuardianFagm());
        fragmentList.add(new DataFagm());
        fragmentList.add(new SettingFrgm());
        fragmentList.add(new MeFrgm());
        NavigationController navigationController = tab.custom().addItem(tabsMessage).addItem(tabsContact).addItem(tabsWork).addItem(tabsChart).addItem(tabsMine).build();
        viewPager.setAdapter(new ViewpagerFragmentAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setOffscreenPageLimit(5);
        //自动适配ViewPager页面切换
        navigationController.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(2);
    }


    /**
     * 正常tab
     */
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        SpecialTab mainTab = new SpecialTab(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xffb8bfcc);
        mainTab.setTextCheckedColor(0xff689df8);
        return mainTab;
    }

    /**
     * 中间圆形圆形tab
     */
    private BaseTabItem newRoundItem(int drawable, int checkedDrawable, String text) {
        SpecialTabRound mainTab = new SpecialTabRound(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xffb8bfcc);
        mainTab.setTextCheckedColor(0xff689df8);
        return mainTab;
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


}
