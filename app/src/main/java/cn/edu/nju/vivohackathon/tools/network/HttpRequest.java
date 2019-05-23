package cn.edu.nju.vivohackathon.tools.network;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpRequest {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_XML = MediaType.parse("application/xml; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");//mdiatype 这个需要和服务端保持一致

    private static final String TAG = HttpRequest.class.getSimpleName();

    private static volatile HttpRequest mInstance;//单例引用

    private OkHttpClient mOkHttpClient;//okHttpClient 实例
    private Handler threadHandler;//全局处理子线程和M主线程通信


    private HttpRequest(Context context) {
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        //初始化Handler
        threadHandler = new Handler(context.getMainLooper());
    }

    public static HttpRequest getInstance(Context context) {
        HttpRequest inst = mInstance;
        if (inst == null) {
            synchronized (HttpRequest.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new HttpRequest(context.getApplicationContext());
                    mInstance = inst;
                }
            }
        }
        return inst;
    }


    public Call get(@NonNull final String url, @NonNull final HttpRequestCallback callback) {
        return sendRequest(url, null, callback);
    }

    public Call post(@NonNull final String url, @NonNull String json, @NonNull final HttpRequestCallback callback) {
        return sendRequest(url, RequestBody.create(MEDIA_TYPE_JSON, json), callback);
    }

    public Call post(@NonNull final String url, @NonNull JSONObject jsonObject, @NonNull final HttpRequestCallback callback) {
        return sendRequest(url, RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toJSONString()), callback);
    }

    /**
     * 发送Get/Post请求.
     *
     * @param url         url
     * @param requestBody 若为Null则请求为Get,否则为Post.
     * @param callback
     */
    private Call sendRequest(@NonNull final String url, @Nullable RequestBody requestBody, @NonNull final HttpRequestCallback callback) {
        Log.i(TAG, "Get:" + url);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.header("Connection","keep-alive");
        builder.header("Cookie",CookieManager.getCookie());
        if (requestBody != null) {
            builder.post(requestBody);
        }
        final Request request = builder.build();
        final Call call = mOkHttpClient.newCall(request);

        try {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(TAG, "Http failed.");
                    failedCallBack(e.getMessage(), callback);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG, "Http succ.");
                    //handle set-cookie
                    CookieManager.setCookie(response.header("Set-Cookie"));
                    CookieManager.setCookie(response.header("set-cookie"));
                    successCallback(response, callback);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return call;
    }


    private <T> void successCallback(final Response response, final HttpRequestCallback callBack) {
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onSucc(response);
                }
            }
        });
    }

    private <T> void failedCallBack(final String errorMsg, final HttpRequestCallback callBack) {
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onError(errorMsg);
                }
            }
        });
    }
}
