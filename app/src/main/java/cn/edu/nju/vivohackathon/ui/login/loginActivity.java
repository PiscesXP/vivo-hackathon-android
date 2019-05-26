package cn.edu.nju.vivohackathon.ui.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.nju.vivohackathon.R;
import cn.edu.nju.vivohackathon.businesslogic.comment.Comment;
import cn.edu.nju.vivohackathon.tools.network.HttpRequestCallback;
import okhttp3.Response;

public class loginActivity extends AppCompatActivity implements HttpRequestCallback {


    private Comment mComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        
    }



    @Override
    public void onSucc(Response response) {

    }

    @Override
    public void onError(String errorMsg) {

    }
}
