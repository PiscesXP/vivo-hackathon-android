package cn.edu.nju.vivohackathon.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import cn.edu.nju.vivohackathon.MainActivity;
import cn.edu.nju.vivohackathon.R;
import cn.edu.nju.vivohackathon.tools.network.powerpost.PowerPost;
import cn.edu.nju.vivohackathon.tools.network.powerpost.PowerPostCallback;

public class LoginActivity extends AppCompatActivity implements PowerPostCallback {



    public final int Request_Login  = 1 ;
    public final int Request_Register = 2 ;

    private final static String TAG = LoginActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GoMain();

        //设置listeners
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.etUsername)).getText().toString();
                String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
                if (username.matches("^.{6,16}$") && password.matches("^.{6,16}$")) {
                    loginPost(username,password);
                } else {
                    Toast.makeText(getApplicationContext(), "账号/密码太短", Toast.LENGTH_SHORT).show();
                }
            }
        });


        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.etUsername)).getText().toString();
                String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
                if (username.matches("^.{6,16}$") && password.matches("^.{6,16}$")) {
                    registerPost(username,password);
                } else {
                    Toast.makeText(getApplicationContext(), "账号/密码太短", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onFail(int reqID, String errorMessage) {
        Toast.makeText(this,R.string.network_error, Toast.LENGTH_LONG).show();
        Log.e(TAG,errorMessage);
    }

    @Override
    public void onSuccess(int reqID, JSONObject resultJson) {
        switch (reqID){
            case Request_Login:GoMain();break;
            case Request_Register:RegisterSuccess();break;
        }

    }

    public void loginPost(String username,String password){
        PowerPost
                .request(Request_Login,getApplicationContext(),"login")
                .data("userName",username)
                .data("password",password)
                .callback(this);
    }

    public void registerPost(String username,String password){
        PowerPost
                .request(Request_Register,getApplicationContext(),"register")
                .data("userName",username)
                .data("password",password)
                .callback(this);
    }


    public void GoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        this.finish();
    }

    public void RegisterSuccess(){
        Toast.makeText(this,R.string.registerSuc, Toast.LENGTH_LONG).show();
    }
}
