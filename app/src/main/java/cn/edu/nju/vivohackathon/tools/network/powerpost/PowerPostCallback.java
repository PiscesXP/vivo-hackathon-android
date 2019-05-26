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

    protected void request(Context context, final String url, JSONObject requestJson) {

        HttpRequest.getInstance(context).post(url, requestJson, new HttpRequestCallback() {
            @Override
            public void onSucc(Response response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "Response not successful: " + url);
                    onFail("Response unsuccessful");
                    return;
                }
                try {
                    ResponseBody responseBody = response.body();
                    JSONObject resultJson;
                    if (responseBody != null) {
                        resultJson = JSONObject.parseObject(responseBody.string());
                    } else {
                        resultJson = new JSONObject();
                    }
                    onSuccess(resultJson);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    onFail(e.getMessage());
                }
            }

            @Override
            public void onError(String errorMsg) {
                Log.e(TAG, errorMsg);
                onFail(errorMsg);
            }
        });
    }

    public abstract void onFail(String errorMessage);

    public abstract void onSuccess(JSONObject resultJson);
}
