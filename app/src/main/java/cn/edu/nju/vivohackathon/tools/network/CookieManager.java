package cn.edu.nju.vivohackathon.tools.network;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class CookieManager {

    private static Map<String, String> sCookieMap = new HashMap<>();
    private static final String TAG = CookieManager.class.getSimpleName();

    public static void setCookie(@Nullable String cookie) {
        if (cookie == null || cookie.isEmpty()) {
            return;
        }
        Log.i(TAG, "Got cookie:" + cookie);
        for (String pair : cookie.split(";")) {
            String splited[] = pair.replaceAll("\\s", "").split("=");
            if (splited.length == 2) {
                sCookieMap.put(splited[0], splited[1]);
            }
        }
        Log.i(TAG, "Set cookie:" + cookie);
    }

    public static String getCookie() {
        String cookie = "";
        for (String key : sCookieMap.keySet()) {
            if (key.equals("Path")) {
                continue;
            }
            String value = sCookieMap.get(key);
            cookie += key + "=" + value;
            cookie += ";";
        }
        if (cookie.length() > 0) {
            cookie = cookie.substring(0, cookie.length() - 1);
        }
        Log.i(TAG, "Put cookie:" + cookie);
        return cookie;
    }

}
