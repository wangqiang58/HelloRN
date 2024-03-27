package cn.xdf.ucan.troy.lib.network.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * @ClassName: PingUtils
 * @Description: java类作用描述
 * @Author: mauto
 * @Date: 2022/11/22 14:32
 */
public class PingUtils {

    public final static int PING_SUCCESS = 100;
    public final static int PING_TIMEOUT = 101;
    public final static int PING_FAILED = 102;

    private Handler pingHandler = null;
    private HandlerThread pingThread = null;
    public PingUtils() {
        String pingTag = "ping_thread" + "_" + System.currentTimeMillis();
        pingThread = new HandlerThread(pingTag);
        pingThread.start();
        pingHandler = new Handler(pingThread.getLooper());
    }

    /**
     * @param address  ping 地址
     * @param timeoutOnce 平均超时时间，单位毫秒。注意，是平均
     * @param pingTimes ping次数
     * @param mainThreadHandler 主线程工作回调
     */
    public void ping(String address, int timeoutOnce, int pingTimes, @NonNull Handler mainThreadHandler) {

        pingTimes = pingTimes <= 0 ? 1 : pingTimes;

        int finalPingTimes = pingTimes;
        pingHandler.post(() -> {
            String line = null;
            int tmpPingTimes = 0;
            try {
                Process pro = Runtime.getRuntime().exec("ping " + address);
                BufferedReader buf = new BufferedReader(new InputStreamReader(
                        pro.getInputStream()));
                while ((line = buf.readLine()) != null) {
                    if (line.contains("time")) {
                        tmpPingTimes += 1;
                        if (tmpPingTimes >= finalPingTimes) {
                            pro.destroy();
                            buf.close();
                            mainThreadHandler.sendEmptyMessage(PING_SUCCESS);
                            pingThread.quit();
                            pingHandler.removeCallbacksAndMessages(null);
                        }
                    }
                }
            } catch (Exception ex) {
//                mainThreadHandler.sendEmptyMessage(PING_FAILED);
//                Log.e(">>ping<<", ">>===>> failed ");
                pingThread.quit();
                pingHandler.removeCallbacksAndMessages(null);
            }
        });
        pingHandler.postDelayed(() -> {
            mainThreadHandler.sendEmptyMessage(PING_TIMEOUT);
//            Log.e(">>ping<<", ">>===>> timeout ");
            pingThread.quit();
            pingHandler.removeCallbacksAndMessages(null);
        }, timeoutOnce * pingTimes);
    }

    /**
     * 这个方法，ping之后会返回每次的ping时长
     * @param address ping地址
     * @param timeoutLowerLimit 下限超时，超过这个值就是超市，会返回PING_TIMEOUT
     * @param timeoutUpperLimit 上限超时，所有ping必须在这个值之内完成。没有完成的，将会返回PING_TIMEOUT，并且timeout=timeoutUpperLimit
     * @param mainThreadHandler 主线程工作回调
     */
    public static void pingWithThreshold(String address, int timeoutLowerLimit, int timeoutUpperLimit, @NonNull Handler mainThreadHandler) {
        if (timeoutUpperLimit < timeoutLowerLimit) {
            timeoutUpperLimit = timeoutLowerLimit;
        }
        String pingTag = "ping_thread" + "_" + System.currentTimeMillis();
        HandlerThread pingThread = new HandlerThread(pingTag);
        pingThread.start();

        Handler pingHandler = new Handler(pingThread.getLooper());
        pingHandler.post(() -> {
            String line = null;
            try {
                Log.e(">>ping<<", ">>====> " + address);
                Process pro = Runtime.getRuntime().exec("ping " + address);
                BufferedReader buf = new BufferedReader(new InputStreamReader(
                        pro.getInputStream()));
                while ((line = buf.readLine()) != null) {
                    Log.e(">>ping<<", ">>====> " + line);
                    if (line.contains("time")) {
                        pro.destroy();
                        buf.close();

                        int startIndex = line.indexOf("time=");
                        int endIndex = line.indexOf("ms");
                        String pingTimeStr = line.substring(startIndex + 5, endIndex);
                        int pingTime = Integer.valueOf(pingTimeStr.substring(0, pingTimeStr.indexOf(".")));

                        Message msg = mainThreadHandler.obtainMessage();
                        JSONObject pingObject = new JSONObject();
                        pingObject.putOpt("domain", address);
                        pingObject.putOpt("ping", pingTime);
                        msg.obj = pingObject;

                        if (pingTime > timeoutLowerLimit) {
                            msg.what = PING_TIMEOUT;
                        } else {
                            msg.what = PING_SUCCESS;
                        }

                        mainThreadHandler.sendMessage(msg);
                        Log.e(">>ping<<", ">>====> " + pingTime + " " + msg.what);
                    }
                }
            } catch (Exception ex) {
                pingThread.quit();
                pingHandler.removeCallbacksAndMessages(null);
            }
        });
        int finalTimeoutUpperLimit = timeoutUpperLimit;
        pingHandler.postDelayed(() -> {
            Message msg = mainThreadHandler.obtainMessage();
            JSONObject pingObject = new JSONObject();
            try {
                pingObject.putOpt("domain", address);
                pingObject.putOpt("ping", finalTimeoutUpperLimit);
            } catch (JSONException e) {

            }
            msg.obj = pingObject;
            msg.what = PING_TIMEOUT;
            mainThreadHandler.sendMessage(msg);

            Log.e(">>ping<<", ">>====> 超时");

            pingThread.quit();
            pingHandler.removeCallbacksAndMessages(null);
        }, timeoutUpperLimit);
    }
}
