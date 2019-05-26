package cn.edu.nju.vivohackathon.tools.network.powerpost;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import cn.edu.nju.vivohackathon.tools.network.HttpRequest;
import cn.edu.nju.vivohackathon.tools.network.HttpRequestCallback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class PowerPostCallback {
    private static final String TAG = PowerPostCallback.class.getSimpleName();

    private static final int MAX_RETRIES = 2;

    private int mRetry = 0;

    protected void request(Context context, final String url, JSONObject requestJson) {
        sendRequest(context, url, requestJson);
    }

    private void sendRequest(final Context context, final String url, final JSONObject requestJson) {
        HttpRequest.getInstance(context).post(url, requestJson, new HttpRequestCallback() {
            @Override
            public void onSucc(Response response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Response not successful: " + url);
                    onFail("Response unsuccessful");
                    return;
                }
                try {
                    ResponseBody responseBody = response.body();
                    JSONObject resultJson;
                    if (responseBody != null) {
                        String responseText = responseBody.string();
                        Log.i(TAG, String.format("Response text from %s: %s", url, responseText));
                        resultJson = JSONObject.parseObject(responseText);
                    } else {
                        Log.i(TAG, String.format("Response from %s without body.", url));
                        resultJson = new JSONObject();
                    }
                    onSuccess(resultJson);
                } catch (Exception e) {
                    Log.e(TAG, String.format("Exception on parsing json from %s: %s", url, e.toString()));
                    onFail(e.toString());
                }
            }

            @Override
            public void onError(String errorMsg) {
                Log.e(TAG, String.format("Error on request %s, errorMessage:%s", url, errorMsg));
                if (++mRetry <= MAX_RETRIES) {
                    Log.i(TAG, String.format("Retrying %s... (%d/%d)", url, mRetry, MAX_RETRIES));
                    sendRequest(context, url, requestJson);
                } else {
                    Log.e(TAG, String.format("Request %s failed after %d retries.", url, mRetry));
                    onFail(errorMsg);
                }
            }
        });
    }

    public abstract void onFail(String errorMessage);

    public abstract void onSuccess(JSONObject resultJson);
}
