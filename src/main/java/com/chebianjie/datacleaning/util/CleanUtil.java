package com.chebianjie.datacleaning.util;

/**
 * @author zhengdayue
 * @date: 2021-08-05
 */
public class CleanUtil {

    public static int computeTotalPage(long total) {
        int pageSize = 1000;
        int totalPage = (int)total / pageSize;
        long mod = total % pageSize;
        if (mod != 0) {
            ++totalPage;
        }
        return totalPage;
    }
}
