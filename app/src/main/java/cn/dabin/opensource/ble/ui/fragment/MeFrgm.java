package cn.dabin.opensource.ble.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.helpers.GlideUtil;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseFragment;
import cn.dabin.opensource.ble.network.BleApi;
import cn.dabin.opensource.ble.network.bean.BabyInfoBean;
import cn.dabin.opensource.ble.network.callback.MineStringCallback;
import cn.dabin.opensource.ble.ui.activity.BabyInfoAct;
import cn.dabin.opensource.ble.ui.activity.HomeAct;
import cn.dabin.opensource.ble.ui.activity.TextAct;
import cn.dabin.opensource.ble.util.StringUtils;
import github.opensource.dialog.sneaker.RoundedImageView;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.fragment
 * Created by :  dabin.
 * Created time: 2019/8/27 16:59
 * Changed by :  dabin.
 * Changed time: 2019/8/27 16:59
 * Class description:
 */
public class MeFrgm extends BaseFragment implements View.OnClickListener {


    private TextView tvTitle;
    private RoundedImageView ivHead;
    private TextView tvNickname;
    private TextView tvWorkNumber;
    private AppCompatTextView tvBaby;
    private AppCompatTextView tvUpdate;
    private AppCompatTextView tvDesc;
    private AppCompatTextView tvAbout;
    private Button btnUnlogin;

    @Override protected int getLayoutId() {
        return R.layout.frgm_me;
    }

    @Override public void onLazyLoad() {
        tvTitle = view.findViewById(R.id.tv_title);
        ivHead = view.findViewById(R.id.iv_head);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvWorkNumber = view.findViewById(R.id.tv_work_number);
        tvBaby = view.findViewById(R.id.tv_baby);
        tvUpdate = view.findViewById(R.id.tv_update);
        tvDesc = view.findViewById(R.id.tv_desc);
        tvAbout = view.findViewById(R.id.tv_about);
        btnUnlogin = view.findViewById(R.id.btn_unlogin);
        tvUpdate.setOnClickListener(this);
        tvBaby.setOnClickListener(this);
        btnUnlogin.setOnClickListener(this);
        tvDesc.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
        initDate();
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (this.getActivity() == null) {
                return;
            }
            ImmersionBar.with(this)
                    .statusBarView(R.id.view)
                    .statusBarColor(R.color.colorWhite)
                    .navigationBarEnable(false)
                    .statusBarDarkFont(true)
                    .init();


        }
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update:
                ((HomeAct) getActivity()).sendMsg("v");
                break;
            case R.id.tv_baby:
                BabyInfoAct.openAct(getContext());
                break;
            case R.id.btn_unlogin:
                removeToken();
                getActivity().finish();
                break;
            case R.id.tv_about:
                TextAct.openAct(getContext(), "关于迈宝", getResources().getString(R.string.app_about));
                break;
            case R.id.tv_desc:
                TextAct.openAct(getContext(), "使用说明", getResources().getString(R.string.app_describle));
                break;
            default:
                break;

        }
    }

    private void initDate() {
        HttpParams params = new HttpParams();
        params.put("userMobile", readMobile());
        OkGo.<String>get(BleApi.getUrl(BleApi.getLoginUser)).params(params).tag(this).execute(new MineStringCallback() {
            @Override public void success(String result) {
                BabyInfoBean bean = new Gson().fromJson(result, BabyInfoBean.class);
                if (bean.getSuccess()) {
                    tvNickname.setText(StringUtils.value(bean.getModel().getUsername()));
                    if (StringUtils.isNotEmpty(bean.getModel().getPhoto())) {
                        Glide.with(getContext()).load(bean.getModel().getPhoto()).apply(GlideUtil.$().getOption()).into(ivHead);
                    }
                    tvWorkNumber.setText(StringUtils.value(bean.getModel().getId()));
                } else {
                    showCenterInfoMsg(bean.getMessage());
                }
            }
        });

    }


}
