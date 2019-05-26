package cn.edu.nju.vivohackathon.tools.network.powerpost;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import cn.edu.nju.vivohackathon.tools.network.HttpRequest;
import cn.edu.nju.vivohackathon.tools.network.HttpRequestCallback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PowerPostExecutor {
    private static final String TAG = PowerPostExecutor.class.getSimpleName();

    private static final int MAX_RETRIES = 2;

    private int mRetry = 0;
    private int mReqID;

    private PowerPostCallback powerPostCallback;

    public PowerPostExecutor(PowerPostCallback powerPostCallback) {
        this.powerPostCallback = powerPostCallback;
    }

    public void request(int reqID, Context context, final String url, JSONObject requestJson) {
        this.mReqID = reqID;
        sendRequest(context, url, requestJson);
    }

    private void sendRequest(final Context context, final String url, final JSONObject requestJson) {
        HttpRequest.getInstance(context).post(url, requestJson, new HttpRequestCallback() {
            @Override
            public void onSucc(Response response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Response not successful: " + url);
                    powerPostCallback.onFail(mReqID, "Response unsuccessful");
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
                    powerPostCallback.onSuccess(mReqID, resultJson);
                } catch (Exception e) {
                    Log.e(TAG, String.format("Exception on parsing json from %s: %s", url, e.toString()));
                    powerPostCallback.onFail(mReqID, e.toString());
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
                    powerPostCallback.onFail(mReqID, errorMsg);
                }
            }
        });
    }

}
