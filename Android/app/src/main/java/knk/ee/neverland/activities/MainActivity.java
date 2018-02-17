package knk.ee.neverland.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import knk.ee.neverland.R;
import knk.ee.neverland.fragments.FeedFragment;
import knk.ee.neverland.fragments.GroupsFragment;
import knk.ee.neverland.fragments.ProfileFragment;
import knk.ee.neverland.fragments.QuestsFragment;
import knk.ee.neverland.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeBottomNavigationBar();
        // openLoginActivity();
    }

    private void initializeBottomNavigationBar() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_navigation);

        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);

        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_feed:
                        setMainFragment(new FeedFragment());
                        break;

                    case R.id.navigation_quests:
                        setMainFragment(new QuestsFragment());
                        break;

                    case R.id.navigation_search:
                        setMainFragment(new SearchFragment());
                        break;

                    case R.id.navigation_groups:
                        setMainFragment(new GroupsFragment());
                        break;

                    case R.id.navigation_profile:
                        setMainFragment(new ProfileFragment());
                        break;
                }

                return true;
            }
        });
    }

    private void setMainFragment(final Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_page_frame, fragment);
        fragmentTransaction.commit();
    }

    private void openLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }
}
