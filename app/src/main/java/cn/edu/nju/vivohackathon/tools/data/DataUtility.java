package cn.edu.nju.vivohackathon.tools.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import cn.edu.nju.vivohackathon.R;

public class DataUtility {
    private static final String TAG = DataUtility.class.getSimpleName();

    //---------------------------------------
    //存储key-value
    public static void setSharedValue(Context context, String key, String value) {
        getSharedPreference(context).edit().putString(key, value).apply();
    }

    @Nullable
    public static String getSharedValue(Context context, String key) {
        return getSharedPreference(context).getString(key, null);
    }

    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(context.getString(R.string.preference_shared), Context.MODE_PRIVATE);
    }
    //---------------------------------------


    //---------------------------------------
    //读取/存储文件
    public static String readFile(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        Log.i(TAG, "Reading file:" + path);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return stringBuilder.toString();
    }


    //---------------------------------------
}
