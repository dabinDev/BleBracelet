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
    public static final String getcode = "getLoginCode";
    public static final String login = "login";
    private static String BaseUrl = "http://47.98.150.224:8899/";

    public static String getUrl(String url) {
        return BaseUrl + url;
    }
}
