package cn.edu.nju.vivohackathon.ui.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.nju.vivohackathon.R;
import cn.edu.nju.vivohackathon.businesslogic.account.UserInfo;
import cn.edu.nju.vivohackathon.businesslogic.comment.Comment;
import cn.edu.nju.vivohackathon.tools.network.HttpRequestCallback;
import okhttp3.Response;

public class loginActivity extends AppCompatActivity implements HttpRequestCallback {


    private UserInfo mUserInfo;
    private Comment mComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mUserInfo = new UserInfo(getApplicationContext(), this);
        mComment = new Comment(getApplicationContext(),this);

        //设置listeners
        Button loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUserInfo.isLogin()){
                    Toast.makeText(getApplicationContext(),"账号已经登录",Toast.LENGTH_SHORT).show();
                    return;
                }
                String username = ((EditText) findViewById(R.id.etUsername)).getText().toString();
                String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
                if (username.matches("^.{6,16}$") && password.matches("^.{6,16}$")) {
                    mUserInfo.register(username, password);
                } else {
                    Toast.makeText(getApplicationContext(), "账号/密码太短", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public void onSucc(Response response) {

    }

    @Override
    public void onError(String errorMsg) {

    }
}
