package cn.edu.nju.vivohackathon.businesslogic.comment;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.alibaba.fastjson.JSONObject;

import cn.edu.nju.vivohackathon.R;
import cn.edu.nju.vivohackathon.businesslogic.account.UserInfo;
import cn.edu.nju.vivohackathon.tools.network.HttpRequest;
import cn.edu.nju.vivohackathon.tools.network.HttpRequestCallback;
import okhttp3.Response;

public class Comment {
    private Context mContext;
    private AppCompatActivity mAppCompatActivity;

    private static final String TAG = UserInfo.class.getSimpleName();

    public Comment(Context context, AppCompatActivity appCompatActivity) {
        mContext = context;
        mAppCompatActivity = appCompatActivity;
    }

    public void addComment(int gameID, String comment){
        Log.i(TAG,"Adding comment of game:" + gameID);
        JSONObject json = new JSONObject();
        json.put("gameID",gameID);
        json.put("content",comment);
        HttpRequest.getInstance(mContext).post("addComment", json, new HttpRequestCallback() {
            @Override
            public void onSucc(Response response) {
                if(response.isSuccessful()){
                    Toast.makeText(mContext,mAppCompatActivity.getString(R.string.comment_success),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String errorMsg) {
                Log.e(TAG,errorMsg);
            }
        });
    }

    public void getCommentList(int gameID){

        Log.i(TAG,"Getting comment of game:" + gameID);
        JSONObject json = new JSONObject();
        json.put("gameID",gameID);
        HttpRequest.getInstance(mContext).post("getCommentList", json, new HttpRequestCallback() {
            @Override
            public void onSucc(Response response) {
                if(response.isSuccessful()){
                    //TODO
                    JSONObject json = new JSONObject();

                }
            }

            @Override
            public void onError(String errorMsg) {
                Log.e(TAG,errorMsg);
            }
        });
    }


}
