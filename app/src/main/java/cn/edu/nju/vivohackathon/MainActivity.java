package cn.edu.nju.vivohackathon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import cn.edu.nju.vivohackathon.businesslogic.account.UserInfo;
import cn.edu.nju.vivohackathon.tools.network.HttpRequestCallback;
import cn.edu.nju.vivohackathon.tools.network.HttpRequest;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements HttpRequestCallback {
    private TextView mTextMessage;

    private UserInfo mUserInfo;

    private static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_1:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_2:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_3:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_4:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        UserInfo userInfo = new UserInfo(getApplicationContext(),this);

        //读取已保存的账号密码
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString(getString(R.string.preference_username), "");
        String savedPassword = sharedPreferences.getString(getString(R.string.preference_password), "");
        EditText etUsername = findViewById(R.id.etUsername);
        etUsername.setText(savedUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        etPassword.setText(savedPassword);

        //set listeners
        Button loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonClickHandler();
            }
        });

        Button fetchButton = findViewById(R.id.btnFetch);
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchButtonClickHandler();
            }
        });
    }

    private void loginButtonClickHandler() {
        String username = ((EditText) findViewById(R.id.etUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
        JSONObject loginJson = new JSONObject();
        loginJson.put("userName", username);
        loginJson.put("passWord", password);
        HttpRequest.getInstance(this).post("http://192.168.2.210:8080/app/login", loginJson, this);
        //保存登录账号密码
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(getString(R.string.preference_username), username)
                .putString(getString(R.string.preference_password), password)
                .apply();
    }

    private void fetchButtonClickHandler() {
        HttpRequest.getInstance(this).get("http://192.168.2.210:8080/test", this);
    }


    @Override
    public void onSucc(Response response) {
        try {
            String responseText = response.body().string();
            mTextMessage.setText(responseText);
            Toast.makeText(this, responseText, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }


}
