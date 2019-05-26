package cn.edu.nju.vivohackathon;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.nju.vivohackathon.businesslogic.account.UserInfo;
import cn.edu.nju.vivohackathon.businesslogic.comment.Comment;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private UserInfo mUserInfo;
    private Comment mComment;

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
        BottomNavigationView navView = findViewById(R.id.discover_nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

        Button addCommentButton = findViewById(R.id.btnAddComment);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = ((EditText) findViewById(R.id.etUsername)).getText().toString();
                mComment.addComment(0,comment);

            }
        });

        Button getCommentButton = findViewById(R.id.btnGetComment);
        getCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mComment.getCommentList(0);
            }
        });

        //recycle
        RecyclerView recyclerView = findViewById(R.id.recycler_view_record_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter recordAdapter = new RecordAdapter(RecordUtility.readFromFileSystem());
        recyclerView.setAdapter(recordAdapter);
    }


}
