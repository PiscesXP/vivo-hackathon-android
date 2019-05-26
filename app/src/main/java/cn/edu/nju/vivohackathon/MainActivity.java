package cn.edu.nju.vivohackathon;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
public class MainActivity extends AppCompatActivity {


    private Fragment mFragment;
    private AccountFragment accountFragment;
    private CreationFragment creationFragment;
    private DiscoverFragment discoverFragment;
    private FriendFragment friendFragment;

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


    }

    private void initViews() {
        discoverFragment = new DiscoverFragment();
        accountFragment = new AccountFragment();
        friendFragment = new FriendFragment();
        creationFragment = new CreationFragment();
    }

    /**
     * 切换fragment
     * */
    private void switchFragment(Fragment fragment) {
        if (mFragment != fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_fragment, fragment).commit();
            mFragment = fragment;
        }
    }

}
