package cn.edu.nju.vivohackathon.tools.network;

import okhttp3.Response;

public interface HttpRequestCallback {
    /**
     * 响应成功
     */
    void onSucc(Response response);

    /**
     * 响应失败
     */
    void onError(String errorMsg);
}