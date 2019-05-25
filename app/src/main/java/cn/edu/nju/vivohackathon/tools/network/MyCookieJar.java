package cn.edu.nju.vivohackathon.tools.network;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {


    private static ConcurrentMap<String, List<Cookie>> mCookieStore = new ConcurrentHashMap<>();     //线程安全的map
    private static final String TAG = MyCookieJar.class.getSimpleName();


    @Override
    public void saveFromResponse(@NonNull HttpUrl url,@NonNull List<Cookie> newCookieList) {
        final String hostname = url.host();
        List<Cookie> cookieList = mCookieStore.get(hostname);
        if (cookieList != null) {
            //合并list
            cookieList.addAll(newCookieList);
        } else {
            cookieList = newCookieList;
        }
        mCookieStore.put(hostname, cookieList);
    }

    @NonNull
    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        final String hostname = url.host();
        List<Cookie> cookieList = mCookieStore.get(hostname);
        cookieList = cookieList == null ? new ArrayList<Cookie>() : cookieList;
        return removeExpiredCookie(cookieList);
    }

    /**
     * 删除已过期的Cookie.
     */
    private List<Cookie> removeExpiredCookie(@NonNull List<Cookie> cookieList) {
        Date currentDate = new Date();
        long currentTime = currentDate.getTime();
        for (Cookie cookie : cookieList) {
            if (cookie.expiresAt() < currentTime) {
                Log.i(TAG, "Removed cookie:" + cookie.name());
                cookieList.remove(cookie);
            }
        }
        return cookieList;
    }
}
