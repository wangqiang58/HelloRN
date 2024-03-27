package cn.xdf.ucan.troy.lib.utils;

/**
 * @author hulijia
 * @createDate 2021/9/6
 * @description ViewFastClickUtils
 */
public class ViewFastClickUtils {
    private static long lastClickTime = 0;
    private static final long DIFF = 500L;
    private static int lastViewId = -1;

    public static boolean isFastClick() {
        return isFastClick(-1, DIFF);
    }

    public static boolean isFastClick(long diff) {
        return isFastClick(-1, diff);
    }

    public static boolean isFastClick(int viewId) {
        return isFastClick(viewId, DIFF);
    }

    public static boolean isFastClick(int viewId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastViewId == viewId && lastClickTime > 0 && timeD < diff) {
            return true;
        }

        lastClickTime = time;
        lastViewId = viewId;
        return false;
    }
}