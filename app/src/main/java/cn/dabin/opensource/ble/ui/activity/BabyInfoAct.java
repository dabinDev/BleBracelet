package cn.dabin.opensource.ble.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.helpers.GlideUtil;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.base.BaseActivity;
import cn.dabin.opensource.ble.network.BleApi;
import cn.dabin.opensource.ble.network.bean.BabyInfoBean;
import cn.dabin.opensource.ble.network.callback.MineStringCallback;
import cn.dabin.opensource.ble.util.StringUtils;
import github.opensource.dialog.prompt.PromptButton;
import github.opensource.dialog.prompt.PromptButtonListener;
import github.opensource.dialog.prompt.PromptDialog;
import me.shaohui.advancedluban.Luban;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.ui.activity
 * Created by :  dabin.
 * Created time: 2019/9/7 17:03
 * Changed by :  dabin.
 * Changed time: 2019/9/7 17:03
 * Class description:
 */
public class BabyInfoAct extends BaseActivity implements View.OnClickListener, PromptButtonListener {
    private AppCompatTextView tvHederTitle;
    private ImageView ivHederIcon;
    private AppCompatTextView tvMaibaoNumTitle;
    private AppCompatTextView tvMaibaoNumValue;
    private AppCompatTextView tvNicknameTitle;
    private AppCompatTextView tvNicknameValue;
    private PromptDialog promptDialog;
    private PromptButton[] buttons;
    private int optType;//操作类型


    public static void openAct(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, BabyInfoAct.class);
        context.startActivity(intent);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_baby_info);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorWhite)
                .navigationBarEnable(false)
                .statusBarDarkFont(true)
                .init();
        initView();
    }

    private void initView() {
        promptDialog = new PromptDialog(this);
        tvHederTitle = findViewById(R.id.tv_heder_title);
        ivHederIcon = findViewById(R.id.iv_heder_icon);
        tvMaibaoNumTitle = findViewById(R.id.tv_maibao_num_title);
        tvMaibaoNumValue = findViewById(R.id.tv_maibao_num_value);
        tvNicknameTitle = findViewById(R.id.tv_nickname_title);
        tvNicknameValue = findViewById(R.id.tv_nickname_value);
        findViewById(R.id.tv_back).setOnClickListener(this);
        findViewById(R.id.ll_maibao_num).setOnClickListener(this);
        findViewById(R.id.rl_header).setOnClickListener(this);
        findViewById(R.id.ll_nick_name).setOnClickListener(this);
        initDate();
    }

    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.ll_maibao_num:

                break;
            case R.id.rl_header:
                buttons = new PromptButton[3];
                buttons[0] = new PromptButton("取消", this);
                buttons[1] = new PromptButton("拍照", this);
                buttons[2] = new PromptButton("上传图片", this);
                //buttons[1] = new PromptButton("上传音频", this);
                promptDialog.showBottomAlert("", true, buttons);
                break;
            case R.id.ll_nick_name:

                break;

            default:
                break;

        }
    }

    @Override public void onClick(PromptButton button) {
        switch (StringUtils.value(button.getText())) {
            case "上传图片":
                optType = PictureMimeType.ofImage();
                // 进入相册
                PictureSelector.create(this).openGallery(optType).theme(R.style.picture_default_style).maxSelectNum(1).minSelectNum(1).selectionMode(PictureConfig.MULTIPLE).compressGrade(Luban.CUSTOM_GEAR).isCamera(false).compress(true).compressMode(PictureConfig.LUBAN_COMPRESS_MODE).glideOverride(160, 160).previewEggs(true).compressMaxKB(0).previewImage(true).previewVideo(true).isGif(true).openClickSound(false).forResult(100);
                break;
            case "拍照":
                optType = PictureMimeType.ofImage();
                // 单独拍照
                PictureSelector.create(this).openCamera(optType).theme(R.style.picture_default_style).maxSelectNum(1).minSelectNum(1).selectionMode(PictureConfig.SINGLE).previewImage(true).previewVideo(true).compressGrade(Luban.CUSTOM_GEAR).isCamera(false).compress(true).compressMaxKB(20000).compressMode(PictureConfig.LUBAN_COMPRESS_MODE).glideOverride(160, 160).isGif(false).openClickSound(false).forResult(100);
                break;
            default:
                promptDialog.dismiss();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    //todo 上传图片
                    List<LocalMedia> selectImageList = PictureSelector.obtainMultipleResult(data);
                    if (selectImageList != null && selectImageList.get(0) != null) {
                        String path = selectImageList.get(0).getCompressPath();
                        Glide.with(mContext).load(path).apply(GlideUtil.$().getOption()).into(ivHederIcon);

                    }

                    break;
                default:
                    break;
            }
        }
    }


    @Override public void onPause() {
        super.onPause();
    }

    @Override public void onResume() {
        super.onResume();
    }


    private void initDate() {
        HttpParams params = new HttpParams();
        params.put("userMobile", readMobile());
        OkGo.<String>get(BleApi.getUrl(BleApi.getLoginUser)).params(params).tag(this).execute(new MineStringCallback() {
            @Override public void success(String result) {
                BabyInfoBean bean = new Gson().fromJson(result, BabyInfoBean.class);
                if (bean.getSuccess()) {
                    tvMaibaoNumValue.setText(StringUtils.value(bean.getModel().getUsername()));
                    if (StringUtils.isNotEmpty(bean.getModel().getPhoto())) {
                        Glide.with(mContext).load(bean.getModel().getPhoto()).apply(GlideUtil.$().getOption()).into(ivHederIcon);
                    }
                    tvNicknameValue.setText(StringUtils.value(bean.getModel().getNickname()));
                } else {
                    showCenterInfoMsg(bean.getMessage());
                }
            }
        });

    }


}
