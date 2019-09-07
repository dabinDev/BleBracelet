package com.darsh.multipleimageselect.helpers;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.darsh.multipleimageselect.R;

/**
 * Project :  yunna.
 * Package name: com.zyf.fwms.commonlibrary.utils
 * Created by :  benjamin.
 * Created time: 2018/9/27 13:46
 * Changed by :  benjamin.
 * Changed time: 2018/9/27 13:46
 * Class description:
 */
public class GlideUtil {
    private static GlideUtil instance = new GlideUtil();

    private GlideUtil() {
    }

    public static GlideUtil $() {
        return instance;
    }

    public RequestOptions getOption() {
        RequestOptions options = new RequestOptions().circleCrop().placeholder(R.mipmap.icon_img_blank)//加载成功之前占位图
                .error(R.mipmap.icon_img_blank)//加载错误之后的错误图
                .fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                .centerCrop().skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE);//跳过磁盘缓存
        return options;
    }

    //登陆失败
    public RequestOptions getImageOption(int error) {
        RequestOptions options = new RequestOptions().circleCrop().placeholder(error)//加载成功之前占位图
                .error(error)//加载错误之后的错误图
                .fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                .centerCrop().skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE);//跳过磁盘缓存
        return options;
    }

    //加载头像
    public RequestOptions getHeaderOption() {
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.icon_img_blank)//加载成功之前占位图
                .error(R.mipmap.icon_img_blank)//加载错误之后的错误图
                .override(400, 400)//指定图片的尺寸
                .fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                .centerCrop().circleCrop()//指定图片的缩放类型为centerCrop （圆形）
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE);//跳过磁盘缓存
        return options;
    }


    //加载园图
    public RequestOptions getRoundOption() {
        RequestOptions options = new RequestOptions().circleCrop().placeholder(R.mipmap.icon_img_blank)//加载成功之前占位图
                .error(R.mipmap.icon_img_blank)//加载错误之后的错误图
                .override(400, 400)//指定图片的尺寸
                .fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                .centerCrop().transform(new GlideCircleTransform()).circleCrop()//指定图片的缩放类型为centerCrop （圆形）
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE);//跳过磁盘缓存
        return options;
    }


}
