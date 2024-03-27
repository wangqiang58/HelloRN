package cn.xdf.ucan.troy.lib.utils;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description UnitUtils
 */
public class UnitUtils {
    private static final double UNIT_DOUBLE = 1024.0 * 1024.0;
    private static final long UNIT_LONG = 1024L * 1024L;

    public static double unitByteToM(long byteSize) {
        return byteSize / UNIT_DOUBLE;
    }

    public static long unitMToByte(int MSize) {
        return MSize * UNIT_LONG;
    }
}