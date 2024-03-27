package cn.xdf.ucan.troy.lib.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description DeviceUtils
 */
public class DeviceUtils {
    private static final String TAG = "DeviceUtils-";
    private static final String DATA_STORE_NAME = DeviceUtils.class.getCanonicalName();
    private static final String BACKUP_DEVICE_UNIQUE_ID = "backup_device_unique_id";
    private static final String BACKUP_MAC_ADDRESS = "backup_mac_address";
    private static final String DEFAULT_VALUE_BACKUP = "";
    private static final String DEFAULT_VALUE_DEVICE_ID = "unknown";

    //MIX 2S
    public static String getModel() {
        return Build.MODEL;
    }

    //Xiaomi
    public static String getBrand() {
        return Build.BRAND;
    }

    //sdm845
    public static String getBoard() {
        return Build.BOARD;
    }

    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getAndroidRelease() {
        return Build.VERSION.RELEASE;
    }

    public static String getCountry(Context context) {
        String result = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.
                    getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                String simCountry = telephonyManager.getSimCountryIso();
                if (simCountry != null && simCountry.length() == 2) {
                    result = simCountry;
                } else if (telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
                    String networkCountry = telephonyManager.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2) {
                        result = networkCountry;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = result == null ? "" : result.toLowerCase(Locale.US);
        Log.e(TAG, "getCountry: " + result);
        return result;
    }

    public static long getTotalMemorySize() {
        long result = 0L;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            String ramInfo = br.readLine();
            if (!TextUtils.isEmpty(ramInfo)) {
                String[] arrayOfRam = ramInfo.split("\\s+");
                result = Long.parseLong(arrayOfRam[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(br);
        }
        return result;
    }

    public static String[] getQm(Context context) {
        String[] qmArray = {"", "", "", "", ""};
        if (!checkPhoneStatePermission(context)) {
            Log.e(TAG, "getQm: Do not has phone state permission");
            return qmArray;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            try {
                String imei;
                Class<?> tMClass = telephonyManager.getClass();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    Method getDeviceIdMethod = tMClass.getMethod("getDeviceId");
                    imei = (String) getDeviceIdMethod.invoke(telephonyManager);
                    qmArray[0] = MD5Utils.md5UpperCase(imei);
                } else {
                    Method getImeiMethod = tMClass.getMethod("getImei", int.class);
                    //PhoneConstants.SIM_ID_1
                    imei = (String) getImeiMethod.invoke(telephonyManager, 0);
                    qmArray[0] = MD5Utils.md5UpperCase(imei);
                    //PhoneConstants.SIM_ID_2
                    imei = (String) getImeiMethod.invoke(telephonyManager, 1);
                    qmArray[1] = MD5Utils.md5UpperCase(imei);
                    //PhoneConstants.SIM_ID_3
                    imei = (String) getImeiMethod.invoke(telephonyManager, 2);
                    qmArray[2] = MD5Utils.md5UpperCase(imei);
                    //PhoneConstants.SIM_ID_4
                    imei = (String) getImeiMethod.invoke(telephonyManager, 3);
                    qmArray[3] = MD5Utils.md5UpperCase(imei);
                }
                Method getMeidMethod = tMClass.getMethod("getMeid");
                String meid = (String) getMeidMethod.invoke(telephonyManager);
                qmArray[4] = MD5Utils.md5UpperCase(meid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return qmArray;
    }

    public static String getDeviceUniqueId(Context context) {
        MMKVStorageUtils dataStore = new MMKVStorageUtils(DATA_STORE_NAME, Context.MODE_PRIVATE);
        String result = dataStore.getString(BACKUP_DEVICE_UNIQUE_ID, DEFAULT_VALUE_BACKUP);
        if (TextUtils.isEmpty(result)) {
            result = getReallyUniqueId(context);
            if (!TextUtils.isEmpty(result)) {
                dataStore.saveString(BACKUP_DEVICE_UNIQUE_ID, result);
            } else {
                result = DEFAULT_VALUE_DEVICE_ID;
            }
        }

        Log.d(TAG, "getDeviceUniqueId: " + result);
        return result;
    }

    /*private static String getReallyUniqueId(Context context) {
        String deviceId = getDeviceId(context);
        String macAddress = getMacAddress(context);
        String androidId = getAndroidId(context);
        //UUID是随机数，清除数据/卸载重装后会改变，因此，当result参数都获取不到时，才使用该值，保证不为null且不重复
        String result = TextUtils.isEmpty(deviceId) && TextUtils.isEmpty(macAddress)
                && TextUtils.isEmpty(androidId) ? getUuid() : deviceId + macAddress + androidId;
        return MD5.md5(context.getPackageName() + result);
    }*/

    private static String getReallyUniqueId(Context context) {
        //deviceId依赖系统权限，macAddress与网络状态有关，都不是很稳定
        //String deviceId = getDeviceId(context);
        //String macAddress = getMacAddress(context);
        String androidId = getAndroidId(context);
        //UUID是随机数，清除数据/卸载重装后会改变，因此，当result参数都获取不到时，才使用该值，保证不为null且不重复
        String result = TextUtils.isEmpty(androidId) ? getUuid() : androidId;
        return MD5Utils.md5UpperCase(context.getPackageName() + result);
    }

    //7d39e1cb1548b22f
    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        String result = null;
        try {
            result = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            //LogUtil.d(TAG, e.getMessage());
        }
        result = result == null ? "" : result;
        Log.d(TAG, "getAndroidId: " + result);
        return result;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getSimSerialNumber(Context context) {
        String result = null;
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            try {
                result = telephonyManager.getSimSerialNumber();
            } catch (Exception e) {
                //LogUtil.d(TAG, e.getMessage());
            }
        }
        result = result == null ? "" : result;
        Log.d(TAG, "getSimSerialNumber: " + result);
        return result;
    }

    public static String deviceId = null;

    /**
     * imei
     *
     * @param context 上下文
     * @return imei
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getDeviceId(Context context) {
        if (deviceId != null) {
            return deviceId;
        }
        String result = null;
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            try {
                result = telephonyManager.getDeviceId();
            } catch (Exception e) {
                //LogUtil.d(TAG, e.getMessage());
            }
        }
        //非空且非全0
        result = TextUtils.isEmpty(result) || result.matches("[0]+") ? "" : result;
        Log.d(TAG, "getDeviceId: " + result);
        deviceId = result;
        return result;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getImsi(Context context) {
        String result = null;
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            try {
                result = telephonyManager.getSubscriberId();
            } catch (Exception e) {
                //LogUtil.d(TAG, e.getMessage());
            }
        }
        result = result == null ? "" : result;
        Log.d(TAG, "getImsi: " + result);
        return result;
    }

    //"unknown"
    @SuppressLint("HardwareIds")
    public static String getDeviceSerial() {
        return Build.SERIAL;
    }

    public static String getUuid() {
        String result = null;
        try {
            result = UUID.randomUUID().toString();
        } catch (Exception e) {
            //LogUtil.d(TAG, e.getMessage());
        }
        result = result == null ? "" : result;
        Log.d(TAG, "getUuid: " + result);
        return result;
    }

    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    private static String getProperties(String key, String defaultValues) {
        String result = defaultValues;
        try {
            @SuppressLint("PrivateApi")
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method getMethod = systemPropertiesClass.getMethod("get",
                    String.class, String.class);
            result = (String) getMethod.invoke(null, key, defaultValues);
        } catch (Exception e) {
            Log.w(TAG, "key: " + key + " " + e.getMessage());
        }
        return result;
    }

    private static void setProperties(String key, String values) {
        try {
            @SuppressLint("PrivateApi")
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method setMethod = systemPropertiesClass.getMethod("set",
                    String.class, String.class);
            setMethod.invoke(null, key, values);
        } catch (Exception e) {
            Log.w(TAG, "key: " + key + " " + e.getMessage());
        }
    }

    public static String getMacAddress(Context context) {
        MMKVStorageUtils dataStore = new MMKVStorageUtils(DATA_STORE_NAME, Context.MODE_PRIVATE);
        String result = dataStore.getString(BACKUP_MAC_ADDRESS, DEFAULT_VALUE_BACKUP);
        if (TextUtils.isEmpty(result)) {
            result = getReallyMacAddress(context);
            if (!TextUtils.isEmpty(result)) {
                dataStore.saveString(BACKUP_MAC_ADDRESS, result);
            }
        }
        Log.d(TAG, "getMacAddress: " + result);
        return result;
    }

    private static String getReallyMacAddress(Context context) {
        String result;
        int androidVersionM = 23;
        if (getAndroidVersion() < androidVersionM) {
            result = getMacAddressWifiInfo(context);
        } else {
            result = getMacAddressNetwork();
        }
        result = TextUtils.isEmpty(result) || "02:00:00:00:00:00".equals(result) ? "" : result;
        Log.d(TAG, "getMacAddress really: " + result);
        return result;
    }

    //6.0以后获取的MacAddress固定为02:00:00:00:00:00
    //6.0以前，无网络、wifi、数据业务场景下均能获取MacAddress
    @SuppressLint("HardwareIds")
    private static String getMacAddressWifiInfo(Context context) {
        String result = "";
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                result = wifiInfo.getMacAddress();
                if (!TextUtils.isEmpty(result)) {
                    result = result.toUpperCase();
                }
            }
        }
        result = result == null ? "" : result;
        Log.d(TAG, "getMacAddressWifiInfo: " + result);
        return result;
    }

    //通过网络接口获取MacAddress，wifi未连接情况下无法获取MacAddress
    private static String getMacAddressNetwork() {
        String result = null;
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration
                    = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                if (networkInterface == null) {
                    continue;
                }
                if (!"wlan0".equalsIgnoreCase(networkInterface.getName())) {
                    continue;
                }
                byte[] addressByteArray = networkInterface.getHardwareAddress();
                if (addressByteArray == null || addressByteArray.length == 0) {
                    continue;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (byte addressByte : addressByteArray) {
                    stringBuilder.append(String.format("%02X:", addressByte));
                }
                if (stringBuilder.length() > 0) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                result = stringBuilder.toString();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = result == null ? "" : result;
        Log.d(TAG, "getMacAddressNetwork: " + result);
        return result;
    }

    //通过IP地址获取MacAddress
    //wifi连接情况下可以获取MacAddress，与getMacAddressNetwork方法返回值相同
    //数据业务下可以获取MacAddress，    与getMacAddressNetwork方法返回值不同
    private static String getMacAddressNetAddress() {
        String result = null;
        try {
            InetAddress inetAddress = getLocalNetAddress();
            byte[] addressByteArray = NetworkInterface.getByInetAddress(inetAddress)
                    .getHardwareAddress();
            StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0; i < addressByteArray.length; i++) {
                if (i != 0) {
                    stringBuffer.append(':');
                }
                String str = Integer.toHexString(addressByteArray[i] & 0xFF);
                if (!TextUtils.isEmpty(str)) {
                    stringBuffer.append(str.length() == 1 ? 0 + str : str);
                }
            }
            result = stringBuffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = result == null ? "" : result;
        Log.d(TAG, "getMacAddressInetAddress: " + result);
        return result;
    }

    private static InetAddress getLocalNetAddress() {
        InetAddress result = null;
        try {
            for (Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface
                    .getNetworkInterfaces(); networkInterfaceEnumeration.hasMoreElements(); ) {
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                if (networkInterface == null) {
                    continue;
                }
                Enumeration<InetAddress> netAddressEnumeration = networkInterface.getInetAddresses();
                if (netAddressEnumeration == null) {
                    continue;
                }
                while (netAddressEnumeration.hasMoreElements()) {
                    InetAddress inetAddress = netAddressEnumeration.nextElement();
                    if (inetAddress != null && !inetAddress.isLoopbackAddress()
                            && !inetAddress.getHostAddress().contains(":")) {
                        result = inetAddress;
                        break;
                    }
                }
                if (result != null) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //8.0以后无法获取MACAddress，8.0以前wifi未连接情况下无法获取MacAddress
    private static String getMACAddressShell() {
        String result = null;
        LineNumberReader lineNumberReader = null;
        try {
            Process process = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            lineNumberReader = new LineNumberReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = lineNumberReader.readLine()) != null) {
                result = line.trim().toUpperCase();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (lineNumberReader != null) {
                try {
                    lineNumberReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        result = result == null ? "" : result;
        Log.d(TAG, "getMACAddressShell: " + result);
        return result;
    }

    //8.0以后无法获取MACAddress，8.0以前wifi未连接情况下无法获取MacAddress
    private static String getMACAddressFile() {
        String result = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("/sys/class/net/wlan0/address");
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[4096];
            int hasRead;
            while ((hasRead = fileReader.read(buffer)) > 0) {
                builder.append(buffer, 0, hasRead);
            }
            result = builder.toString().trim().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        result = result == null ? "" : result;
        Log.d(TAG, "getMACAddressFile: " + result);
        return result;
    }

    public static String getUserAgent() {
        StringBuilder stringBuilder = new StringBuilder();
        String userAgent = System.getProperty("http.agent");
        if (!TextUtils.isEmpty(userAgent)) {
            for (int i = 0; i < userAgent.length(); i++) {
                char c = userAgent.charAt(i);
                if (c <= '\u001f' || c >= '\u007f') {
                    stringBuilder.append(String.format("\\u%04x", (int) c));
                } else {
                    stringBuilder.append(c);
                }
            }
        }
        Log.d(TAG, "userAgent: " + userAgent + " ,result: " + stringBuilder);
        return stringBuilder.toString();
    }

    private static boolean checkPhoneStatePermission(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ||
                context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED
                || context.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE")
                == PackageManager.PERMISSION_GRANTED;
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isBackGround(Context context, String packageName) {
        int i, j;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> apps = am.getRunningAppProcesses();
        if (apps != null) {
            j = apps.size();
            for (i = 0; i < j; i++) {
                if (apps.get(i).processName.equals(packageName)) {
                    if (apps.get(i).importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && apps.get(i).importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getWebViewUA(Context context) {
        WebView webView = new WebView(context);
        return webView.getSettings().getUserAgentString();
    }
}