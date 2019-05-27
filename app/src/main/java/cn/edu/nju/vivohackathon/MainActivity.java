package cn.edu.nju.vivohackathon;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.alibaba.fastjson.JSONObject;

import cn.edu.nju.vivohackathon.tools.network.powerpost.PowerPostCallback;
import cn.edu.nju.vivohackathon.tools.network.powerpost.PowerPost;

public class MainActivity extends AppCompatActivity implements PowerPostCallback {


    private Fragment mFragment;
    private AccountFragment accountFragment;
    private CreationFragment creationFragment;
    private DiscoverFragment discoverFragment;
    private FriendFragment friendFragment;
    private GameFragment gameFragment;

    private static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_1:
                    switchFragment(discoverFragment);
                    return true;
                case R.id.navigation_2:
                    switchFragment(creationFragment);
                    return true;
                case R.id.navigation_3:
                    switchFragment(friendFragment);
                    return true;
                case R.id.navigation_4:
                    switchFragment(accountFragment);
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
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initViews();


        PowerPost
                .request(123,getApplicationContext(),"/login")
                .data("userName","admin")
                .callback(this);

        //设置listeners
        /*
        Button loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserInfo.isLogin()) {
                    Toast.makeText(getApplicationContext(), "账号已经登录", Toast.LENGTH_SHORT).show();
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
                mComment.addComment(0, comment);

            }
        });

        Button getCommentButton = findViewById(R.id.btnGetComment);
        getCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mComment.getCommentList(0);
            }
        });
*/

        //发现页面
        /*
        RecyclerView recyclerView = findViewById(R.id.discover_recycleview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        GameInfoAdapter gameInfoAdapter = new GameInfoAdapter(new ArrayList<GameInfo>());
        recyclerView.setAdapter(gameInfoAdapter);
*/
        //fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, gameFragment).commit();
        mFragment = gameFragment;
}

    private void initViews() {
        discoverFragment = new DiscoverFragment();
        accountFragment = new AccountFragment();
        friendFragment = new FriendFragment();
        creationFragment = new CreationFragment();
        gameFragment = new GameFragment();
    }

    /**
     * 切换fragment
     * */
    private void switchFragment(Fragment fragment) {
        if (mFragment != fragment) {
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(mFragment)
                        .add(R.id.main_fragment, fragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }

    @Override
    public void onFail(int reqID, String errorMessage) {

    }

    @Override
    public void onSuccess(int reqID, JSONObject resultJson) {

    }
}
