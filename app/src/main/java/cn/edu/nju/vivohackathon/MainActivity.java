package cn.edu.nju.vivohackathon;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.edu.nju.vivohackathon.tools.network.HttpRequestCallback;
import cn.edu.nju.vivohackathon.tools.network.HttpRequest;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements HttpRequestCallback {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
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
        HttpRequest.getInstance(this).post("http://192.168.2.141:8080/app/login", loginJson, this);
    }

    private void fetchButtonClickHandler() {
        HttpRequest.getInstance(this).get("http://192.168.2.141:8080/text", this);
    }


    @Override
    public void onSucc(Response response) {
        try {
            mTextMessage.setText(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(String errorMsg) {
        mTextMessage.setText("Error:" + errorMsg);
    }
}
