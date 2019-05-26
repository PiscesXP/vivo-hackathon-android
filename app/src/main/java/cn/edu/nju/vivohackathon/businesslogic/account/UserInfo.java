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
        Log.i(TAG, "login...");
        JSONObject json = new JSONObject();
        json.put("userName", username);
        json.put("passWord", password);
        HttpRequest.getInstance(mContext).post("login", json.toJSONString(), new HttpRequestCallback() {
            @Override
            public void onSucc(Response result) {
                try {
                    JSONObject json = JSONObject.parseObject(result.body().string());
                    int succ = json.getIntValue("success");
                    if (succ == 0) {
                        //登录成功
                        Toast.makeText(mContext, R.string.login_succ, Toast.LENGTH_LONG).show();
                        String userID = json.getString("userID");
                        DataUtility.setSharedValue(mContext, mContext.getString(R.string.preference_userid), userID);
                        //获取用户信息
                        JSONObject reqUserInfoJson = new JSONObject();
                        reqUserInfoJson.put("userID", userID);
                        HttpRequest.getInstance(mContext).post("getUserInfoByID", reqUserInfoJson, new HttpRequestCallback() {
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
                                Toast.makeText(mContext, mAppCompatActivity.getString(R.string.network_error), Toast.LENGTH_LONG).show();
                                Log.e(TAG, errorMsg);
                            }
                        });
                    } else {
                        Toast.makeText(mContext, R.string.login_fail, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(mContext, mAppCompatActivity.getString(R.string.network_error), Toast.LENGTH_LONG).show();
                Log.e(TAG, errorMsg);
            }
        });
    }

    public void register(final String username, final String password) {
        Log.e(TAG, "Registering...");
        JSONObject json = new JSONObject();
        json.put("userName", username);
        json.put("passWord", password);
        HttpRequest.getInstance(mContext).post("register", json, new HttpRequestCallback() {
            @Override
            public void onSucc(Response response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject resultJson = JSONObject.parseObject(response.body().string());
                        if (resultJson.getIntValue("success") == 0) {
                            Toast.makeText(mContext, "注册成功，自动登录中...", Toast.LENGTH_LONG).show();
                            login(username, password);
                        } else {
                            Toast.makeText(mContext, "注册失败，账号已存在", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(mContext, mAppCompatActivity.getString(R.string.network_error), Toast.LENGTH_LONG).show();
                Log.e(TAG, errorMsg);
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
        HttpRequest.getInstance(mContext).post(reqUrl, reqJson, new HttpRequestCallback() {
            @Override
            public void onSucc(Response response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject resultJson = JSONObject.parseObject(response.body().string());
                        if (amount == 0) {
                            int money = resultJson.getIntValue("amount");
                            TextView tvMoney = mAppCompatActivity.findViewById(R.id.tvMoney);
                            tvMoney.setText(money);
                        } else {
                            boolean success = resultJson.get("success") == 0;
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
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onError(String errorMsg) {
                Log.e(TAG, errorMsg);
            }
        });
    }
}
