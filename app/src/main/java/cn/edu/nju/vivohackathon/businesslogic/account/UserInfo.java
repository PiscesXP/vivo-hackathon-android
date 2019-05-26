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
import cn.edu.nju.vivohackathon.tools.network.powerpost.PowerPost;
import cn.edu.nju.vivohackathon.tools.data.DataUtility;
import cn.edu.nju.vivohackathon.tools.network.HttpRequest;
import cn.edu.nju.vivohackathon.tools.network.HttpRequestCallback;
import cn.edu.nju.vivohackathon.tools.network.powerpost.PowerPostCallback;
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
        Log.i(TAG, "login...");
        PowerPost
                .request(mContext, "login")
                .data("userName", username)
                .data("passWord", password)
                .callback(new PowerPostCallback() {
                    @Override
                    public void onFail(String errorMessage) {
                        Toast.makeText(mContext, mAppCompatActivity.getString(R.string.network_error), Toast.LENGTH_LONG).show();
                        Log.e(TAG, errorMessage);
                    }

                    @Override
                    public void onSuccess(JSONObject resultJson) {
                        int succ = resultJson.getIntValue("success");
                        if (succ == 0) {
                            //登录成功
                            Toast.makeText(mContext, R.string.login_succ, Toast.LENGTH_LONG).show();
                            String userID = resultJson.getString("userID");
                            DataUtility.setSharedValue(mContext, mContext.getString(R.string.preference_userid), userID);
                            //获取用户信息
                            JSONObject reqUserInfoJson = new JSONObject();
                            reqUserInfoJson.put("userID", userID);
                            PowerPost.request(mContext, "getUserInfoByID")
                                    .data("userID", userID)
                                    .callback(new PowerPostCallback() {
                                        @Override
                                        public void onFail(String errorMessage) {
                                            Toast.makeText(mContext, mAppCompatActivity.getString(R.string.network_error), Toast.LENGTH_LONG).show();
                                            Log.e(TAG, errorMessage);
                                        }

                                        @Override
                                        public void onSuccess(JSONObject resultJson) {
                                            TextView tvUserName = mAppCompatActivity.findViewById(R.id.tvUserName);
                                            tvUserName.setText(resultJson.getString("name"));

                                            TextView tvMoney = mAppCompatActivity.findViewById(R.id.tvMoney);
                                            tvMoney.setText(resultJson.getIntValue("amount"));

                                            ImageView ivAvatar = mAppCompatActivity.findViewById(R.id.ivAvatar);
                                            String base64Avatar = resultJson.getString("img");
                                            byte[] bytes = Base64.decode(base64Avatar, Base64.DEFAULT);
                                            Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            ivAvatar.setImageBitmap(decodedByte);
                                        }
                                    });
                        } else {
                            Toast.makeText(mContext, R.string.login_fail, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void register(final String username, final String password) {
        Log.e(TAG, "Registering...");
        PowerPost
                .request(mContext, "register")
                .data("userName", username)
                .data("passWord", password)
                .callback(new PowerPostCallback() {
                    @Override
                    public void onFail(String errorMessage) {
                        Log.e(TAG, errorMessage);
                        Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(JSONObject resultJson) {
                        if (resultJson.getIntValue("success") == 0) {
                            Toast.makeText(mContext, "注册成功，自动登录中...", Toast.LENGTH_LONG).show();
                        }
                        //账号已存在
                        login(username, password);
                    }
                });
    }

    /**
     * @param amount 0:查询. >0:充值. <0:消费.
     */
    public void queryMoney(final int amount) {
        Log.i(TAG, "Query money:" + amount);
        final JSONObject reqJson = new JSONObject();
        reqJson.put("amount", amount);
        String reqUrl;
        if (amount == 0) {
            reqUrl = "getMoney";
        } else if (amount > 0) {
            reqUrl = "addMoney";
        } else {
            reqUrl = "spendMoney";
        }
        PowerPost
                .request(mContext, reqUrl)
                .data("amount", amount)
                .callback(new PowerPostCallback() {
                    @Override
                    public void onFail(String errorMessage) {
                        Log.e(TAG, errorMessage);
                    }

                    @Override
                    public void onSuccess(JSONObject resultJson) {
                        if (amount == 0) {
                            int money = resultJson.getIntValue("amount");
                            TextView tvMoney = mAppCompatActivity.findViewById(R.id.tvMoney);
                            tvMoney.setText(money);
                        } else {
                            boolean success = resultJson.getIntValue("success") == 0;
                            queryMoney(0);  //刷新金钱
                            if (amount < 0) {
                                //消费
                                if (success) {
                                    Toast.makeText(mContext, R.string.money_spent, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, R.string.money_spend_fail, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //充值
                                if (success) {
                                    Toast.makeText(mContext, R.string.money_add, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
    }


    public void follow(String userID, boolean isFollow) {
        PowerPost
                .request(mContext, "followUser")
                .data("userID", userID)
                .data("type", isFollow ? 0 : 1)
                .callback(new PowerPostCallback() {
                    @Override
                    public void onFail(String errorMessage) {
                        //TODO
                    }

                    @Override
                    public void onSuccess(JSONObject resultJson) {
                        //TODO
                    }
                });
    }

    public void findUserByName(String userName) {
        PowerPost
                .request(mContext, "findUserByName")
                .data("userName", userName)
                .callback(new PowerPostCallback() {
                    @Override
                    public void onFail(String errorMessage) {
                        //TODO
                    }

                    @Override
                    public void onSuccess(JSONObject resultJson) {
                        String userName = resultJson.getString("name");
                        String userID = resultJson.getString("id");
                        String userAvatarBase64 = resultJson.getString("img");
                        //TODO
                    }
                });
    }
}
