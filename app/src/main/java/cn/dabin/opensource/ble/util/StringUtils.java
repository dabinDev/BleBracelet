package cn.dabin.opensource.ble.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Project :  yunaandroid.
 * Package name: com.renwei.yunlong.utils
 * Created by :  Benjamin.
 * Created time: 2017/11/7 10:35
 * Changed by :  Benjamin.
 * Changed time: 2017/11/7 10:35
 * Class description:
 */

public class StringUtils {
    // 简体中文的编码范围从B0A1（45217）一直到F7FE（63486）
    private static int BEGIN = 45217;
    private static int END = 63486;
    // 按照声 母表示，这个表是在GB2312中的出现的第一个汉字，也就是说“啊”是代表首字母a的第一个汉字。
    // i, u, v都不做声母, 自定规则跟随前面的字母
    private static char[] chartable = {'啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝',};
    // 二十六个字母区间对应二十七个端点
    // GB2312码汉字区间十进制表示
    private static int[] table = new int[27];
    // 对应首字母区间表
    private static char[] initialtable = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 't', 't', 'w', 'x', 'y', 'z',};

    // 初始化
    static {
        for (int i = 0; i < 26; i++) {
            table[i] = gbValue(chartable[i]);// 得到GB2312码的首字母区间端点表，十进制。
        }
        table[26] = END;// 区间表结尾
    }

    public static String value(String text) {
        if (isEmpty(text)) {
            return "";
        } else {
            return String.valueOf(text);
        }
    }


    public static String valueWithEnd(String text) {
        return isEmpty(text) ? "--" : text;
    }

    /**
     * @param object
     * @return
     */
    public static String value(Object object) {
        String text = object == null ? "" : String.valueOf(object);
        if (isEmpty(text)) {
            return "";
        } else {
            return String.valueOf(text);
        }
    }

    public static String value(int text) {
        if ("null".equals(String.valueOf(text))) {
            return "";
        } else {
            return String.valueOf(text);
        }
    }


    public static String value(long text) {
        if ("null".equals(String.valueOf(text))) {
            return "";
        } else {
            return String.valueOf(text);
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    -------------------------------------------------------------------------------
    13(老)号段：130、131、132、133、134、135、136、137、138、139
    14(新)号段：145、147
    15(新)号段：150、151、152、153、154、155、156、157、158、159
    17(新)号段：170、171、173、175、176、177、178
    18(3G)号段：180、181、182、183、184、185、186、187、188、189


    */
        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    /**
     * 字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equalsIgnoreCase(str.trim()) || "null".equalsIgnoreCase(str.trim());
    }

    /**
     * 字符串是否为非空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 字符串是否为空格串
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 字符串是否非空格串
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 将null转换为空串,如果参数为非null，则直接返回
     *
     * @param str
     * @return
     */
    public static String nullToEmpty(String str) {
        return (str == null ? "" : str);
    }

    /**
     * 将null转换为字符串"null",如果参数为非null，则直接返回
     *
     * @param str
     * @return
     */
    public static String nullToString(String str) {
        return (str == null ? "null" : str);
    }

    /**
     * 半角转全角
     *
     * @param half
     * @return 全角字符串.
     */
    public static String halfToFull(String half) {
        if (isEmpty(half)) {
            return half;
        }

        char c[] = half.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }

        return new String(c);
    }

    /**
     * 全角转半角
     *
     * @param full
     * @return 半角字符串
     */
    public static String fullToHalf(String full) {
        if (isEmpty(full)) {
            return full;
        }

        char c[] = full.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }

        String result = new String(c);
        return result;
    }

    /**
     * 处理html中的特殊字符串
     * <p>
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param html
     * @return
     */
    public static String htmlEscapeCharsToString(String html) {
        return isBlank(html) ? html : html.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

      /**
     * 将字符串用指定的编码进行编码，发生异常时，源字符串直接返回，不做编码
     *
     * @param str
     * @param charset
     * @return
     */
    public static String urlEncode(String str, String charset) {
        if (!isEmpty(str)) {
            try {
                return URLEncoder.encode(str, charset);
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }

        return str;
    }

    /**
     * 将字符串用指定的编码进行解码，发生异常时，源字符串直接返回，不做解码
     *
     * @param str
     * @param charset
     * @return
     */
    public static String urlDecode(String str, String charset) {
        if (!isEmpty(str)) {
            try {
                return URLDecoder.decode(str, charset);
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }

        return str;
    }

    /**
     * 手机号隐藏
     *
     * @param phone
     * @return
     */
    public static String phoneHide(String phone) {
        if (!isEmpty(phone)) {
            int length = phone.length();
            String start = phone.substring(0, 3);
            String end = phone.substring(length - 4, length);
            return start + "****" + end;
        }
        return phone;
    }

    public static String cn2py(String SourceStr) {
        String Result = "";
        int StrLength = SourceStr.length();
        int i;
        try {
            for (i = 0; i < StrLength; i++) {
                Result += Char2Initial(SourceStr.charAt(i));
            }
        } catch (Exception e) {
            Result = "";
            e.printStackTrace();
        }
        return Result;
    }

    /**
     * 输入字符,得到他的声母,英文字母返回对应的大写字母,其他非简体汉字返回 '0' 　　*
     */
    private static char Char2Initial(char ch) {
        // 对英文字母的处理：小写字母转换为大写，大写的直接返回
        if (ch >= 'a' && ch <= 'z') {
            return (char) (ch - 'a' + 'A');
        }
        if (ch >= 'A' && ch <= 'Z') {
            return ch;
        }
        // 对非英文字母的处理：转化为首字母，然后判断是否在码表范围内，
        // 若不是，则直接返回。
        // 若是，则在码表内的进行判断。
        int gb = gbValue(ch);// 汉字转换首字母
        if ((gb < BEGIN) || (gb > END))// 在码表区间之前，直接返回
        {
            return ch;
        }
        int i;
        for (i = 0; i < 26; i++) {// 判断匹配码表区间，匹配到就break,判断区间形如“[,)”
            if ((gb >= table[i]) && (gb < table[i + 1])) {
                break;
            }
        }
        if (gb == END) {// 补上GB2312区间最右端
            i = 25;
        }
        return initialtable[i]; // 在码表区间中，返回首字母
    }

    /**
     * 取出汉字的编码 cn 汉字
     */
    private static int gbValue(char ch) {// 将一个汉字（GB2312）转换为十进制表示。
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2) {
                return 0;
            }
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 判断远程服务器文件是否存在
     *
     * @param path
     * @return
     */
    public static void isFileExistInServer(final String path, final FileExistImpl impl) {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).run();
    }

    public interface FileExistImpl {
        void exist();

        void notExist();
    }


}
