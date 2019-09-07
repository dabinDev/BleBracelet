package cn.dabin.opensource.ble.network;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.network
 * Created by :  dabin.
 * Created time: 2019/8/31 2:31
 * Changed by :  dabin.
 * Changed time: 2019/8/31 2:31
 * Class description:
 */
public class BleApi {
    public static final String getcode = "getLoginCode";//获取验证码
    public static final String login = "login";//登录
    public static final String saveShaData = "saveShData";//上传数据
    public static final String getShDataList = "getShDataList";//获取数据
    public static final String saveShYongYan = "saveShYongYan";//上传第三也数据
    public static final String getShYongyan = "getShYongyan";//拉取第三页数据
    public static final String uploadPhoto = "uploadPhoto";//上传图片
    public static final String getLoginUser = "getLoginUser";//获取用户信息
    public static final String userEdit = "userEdit";//编辑用户信息



    private static String BaseUrl = "http://47.98.150.224:8899/";

    public static String getUrl(String url) {
        return BaseUrl + url;
    }
}
