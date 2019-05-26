package cn.edu.nju.vivohackathon.tools.network.powerpost;

import com.alibaba.fastjson.JSONObject;

public interface PowerPostCallback {

    public abstract void onFail(int reqID, String errorMessage);

    public abstract void onSuccess(int reqID, JSONObject resultJson);
}
