package cn.edu.nju.vivohackathon.tools.network;


import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPtool {
    private static final HTTPtool ourInstance = new HTTPtool();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String BASE_URL = "http://localhost:8080/";
    private static final int Default_Timeout = 10000;

    public static HTTPtool getInstance() {
        return ourInstance;
    }

    private HTTPtool() {
    }






    public <T> void AsynGet(String actionUrl, HttpRequestCallback callbackFunc){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().
                connectTimeout(Default_Timeout, TimeUnit.MILLISECONDS)
                .readTimeout(Default_Timeout, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder().url(getURL(actionUrl)).build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println(response.body().string());
                }
            }
        });

    }






    public String post(String url, JSONObject param) throws IOException {
        RequestBody body = RequestBody.create(JSON, param.toString());
        Request request = new Request.Builder()
                .url(getURL(url))
                .post(body)
                .build();
        try (Response response =  new OkHttpClient().newCall(request).execute()) {
            return response.body().string();
        }
    }


    public String getURL(String actionUrl){
        return BASE_URL+"/"+actionUrl;
    }
}
