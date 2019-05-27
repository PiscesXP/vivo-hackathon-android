package cn.edu.nju.vivohackathon;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private Fragment mFragment;
    private AccountFragment accountFragment;
    private CreationFragment creationFragment;
    private DiscoverFragment discoverFragment;
    private FriendFragment friendFragment;
    private GameFragment gameFragment;
    private MiningFragment miningFragment;


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

        //fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, discoverFragment).commit();
        mFragment = discoverFragment;
    }

    private void initViews() {
        discoverFragment = new DiscoverFragment();
        accountFragment = new AccountFragment();
        friendFragment = new FriendFragment();
        creationFragment = new CreationFragment();
        gameFragment = new GameFragment();
        miningFragment = new MiningFragment();
    }

    /**
     * 切换fragment
     */
    private void switchFragment(Fragment fragment) {
        if (mFragment != fragment) {
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(mFragment)
                        .add(R.id.main_fragment, fragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).addToBackStack("").commit();
            }
            mFragment = fragment;
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


    //乱加的
    public void switchToGame(int gameID) {
        switch (gameID) {
            case 1:
                switchFragment(gameFragment);
                break;
            case 2:
                switchFragment(miningFragment);
                break;
        }
    }
}
