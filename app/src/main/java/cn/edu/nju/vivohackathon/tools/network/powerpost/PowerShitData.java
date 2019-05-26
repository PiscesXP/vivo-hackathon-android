package cn.edu.nju.vivohackathon.tools.network.powerpost;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

public class PowerShitData {
    private static final String TAG = PowerShitData.class.getSimpleName();
    private int mReqID;
    private JSONObject mJSONObject;
    private Context mContext;
    private String mUrl;

    protected PowerShitData(int reqID, Context context, final String url) {
        mReqID = reqID;
        mJSONObject = new JSONObject();
        mContext = context;
        mUrl = url;
    }

    public PowerShitData data(String key, Object value) {
        Log.i(TAG, "Add data to JSON:" + key + ", value:" + value);
        mJSONObject.put(key, value);
        return this;
    }

    public void callback(PowerPostCallback powerPostCallback) {
        PowerPostExecutor powerPostExecutor = new PowerPostExecutor(powerPostCallback);
        powerPostExecutor.request(mReqID, mContext, mUrl, mJSONObject);
    }
}
