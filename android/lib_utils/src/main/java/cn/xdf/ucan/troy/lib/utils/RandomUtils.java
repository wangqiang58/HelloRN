package cn.xdf.ucan.troy.lib.utils;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description RandomUtils
 */

import java.util.Random;

/**
 * @author hulijia
 * @createDate 2020/12/9
 * @description RandomUtils
 */
public class RandomUtils {

    /**
     * 获取某个范围内的随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    public static int getRandom(int min, int max) {
        Random random = new Random();
        //nextInt: between zero (inclusive) and {@code max} (exclusive)
        return random.nextInt(max) % (max - min + 1) + min;
    }

}