package cn.dabin.opensource.ble.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseActivity;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.activity
 * Created by :  dabin.
 * Created time: 2019/9/7 17:03
 * Changed by :  dabin.
 * Changed time: 2019/9/7 17:03
 * Class description:
 */
public class BabyInfoAct extends BaseActivity {
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_baby_info);
    }
}
