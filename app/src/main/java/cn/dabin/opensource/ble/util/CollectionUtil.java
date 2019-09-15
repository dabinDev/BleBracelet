package cn.dabin.opensource.ble.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Set;

/**
 * Project :  jbwl.
 * Package name:com.jbwl.agm.lib.base.utils;
 * Created by :  Benjamin.
 * Created time: 2016/9/20 15:04
 * Changed by :  Benjamin.
 * Created time: 2016/9/20 15:04
 * Class description:
 */

public class CollectionUtil {
    /**
     * 判断集合list数据是否为空
     *
     * @param list
     * @return
     */
    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断集合set数据是否为空
     *
     * @param set
     * @return
     */
    public static <T> boolean isEmpty(Set<T> set) {
        return set == null || set.size() == 0;
    }


    public static <T> boolean isEmpty(T[] args) {
        return args == null || args.length == 0;
    }


    public static <T1,T2> boolean isEmpty(Map<T1,T2> map) {
        return map == null || map.size() == 0;
    }


    /**
     * 判断集合list数据是否为非空
     *
     * @param list
     * @return
     */
    public static <T> boolean isNotEmpty(List<T> list) {
        return !isEmpty(list);
    }


    /**
     * 判断集合set数据是否为非空
     *
     * @param set
     * @return
     */
    public static <T> boolean isNotEmpty(Set<T> set) {
        return !isEmpty(set);
    }


    public static <T> boolean isNotEmpty(T[] args) {
        return !isEmpty(args);
    }


    public static <T1,T2> boolean isNotEmpty(Map<T1,T2> map) {
        return !isEmpty(map);
    }


    /**
     * 获取集合list数据长度
     *
     * @param list
     * @return
     */
    public static <T> int getCount(List<T> list) {
        return list == null ? 0 : list.size();
    }


    public static <T> int getCount(T[] args) {
        return args == null ? 0 : args.length;
    }

    /**
     * 获取集合set数据长度
     *
     * @param set
     * @return
     */
    public static <T> int getCount(Set<T> set) {
        return set == null ? 0 : set.size();
    }



    public static <T1,T2> int getCount(Map<T1,T2> map) {
        return map == null ? 0 : map.size();
    }

    /**
     * 反转数据
     *
     * @param sourceList
     * @param <V>
     * @return
     */
    public static <V> List<V> invertList(List<V> sourceList) {
        if (isEmpty(sourceList)) {
            return sourceList;
        }

        List<V> invertList = new ArrayList<V>(sourceList.size());
        for (int i = sourceList.size() - 1; i >= 0; i--) {
            invertList.add(sourceList.get(i));
        }

        return invertList;
    }
}
