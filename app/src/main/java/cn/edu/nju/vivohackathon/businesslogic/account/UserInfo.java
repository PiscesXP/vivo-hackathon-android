package cn.edu.nju.vivohackathon.businesslogic.account;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;


import cn.edu.nju.vivohackathon.R;
import cn.edu.nju.vivohackathon.tools.data.DataUtility;
import cn.edu.nju.vivohackathon.tools.network.HttpRequest;
import cn.edu.nju.vivohackathon.tools.network.HttpRequestCallback;
import okhttp3.Response;

public class UserInfo {
    private Context mContext;
    private AppCompatActivity mAppCompatActivity;

    private static final String TAG = UserInfo.class.getSimpleName();

    public UserInfo(Context context, AppCompatActivity appCompatActivity) {
        mContext = context;
        mAppCompatActivity = appCompatActivity;
    }

    /**
     * @return false if 没找到userid.
     */
    public boolean isLogin() {
        String userID = DataUtility.getSharedValue(mContext, mContext.getString(R.string.preference_userid));
        return userID != null;
    }

    public void login(String username, String password) {
        JSONObject json = new JSONObject();
        json.put("userName", username);
        json.put("passWord", password);
        HttpRequest.getInstance(mContext).post("login", json.toJSONString(), new HttpRequestCallback() {
            @Override
            public void onSucc(Response result) {
                try {
                    JSONObject json = JSONObject.parseObject(result.body().string());
                    int succ = json.getIntValue("success");
                    switch (succ) {
                        case 0:
                            String userID = json.getString("userID");
                            DataUtility.setSharedValue(mContext, mContext.getString(R.string.preference_userid), userID);
                            //获取用户信息
                            JSONObject jsonObject = new JSONObject();
                            json.put("userID", userID);
                            HttpRequest.getInstance(mContext).post("getUserInfoByID", json, new HttpRequestCallback() {
                                @Override
                                public void onSucc(Response response) {
                                    try {
                                        //设置用户信息
                                        JSONObject resultJson = JSONObject.parseObject(response.body().string());

                                        TextView tvUserName = mAppCompatActivity.findViewById(R.id.tvUserName);
                                        tvUserName.setText(resultJson.getString("name"));

                                        TextView tvMoney = mAppCompatActivity.findViewById(R.id.tvMoney);
                                        tvMoney.setText(resultJson.getIntValue("amount"));

                                        ImageView ivAvatar = mAppCompatActivity.findViewById(R.id.ivAvatar);
                                        String base64Avatar = resultJson.getString("img");
                                        byte[] bytes = Base64.decode(base64Avatar, Base64.DEFAULT);
                                        Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        ivAvatar.setImageBitmap(decodedByte);
                                    } catch (Exception e) {
                                        Log.e(TAG, e.getMessage());
                                    }
                                }

                                @Override
                                public void onError(String errorMsg) {
                                    Toast.makeText(mContext,mAppCompatActivity.getString(R.string.network_error),Toast.LENGTH_LONG).show();
                                    Log.e(TAG,errorMsg);
                                }
                            });
                            break;
                        default:
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(mContext,mAppCompatActivity.getString(R.string.network_error),Toast.LENGTH_LONG).show();
                Log.e(TAG,errorMsg);
            }
        });
    }

}
