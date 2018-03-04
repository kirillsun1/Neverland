package knk.ee.neverland.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import knk.ee.neverland.R
import knk.ee.neverland.activities.fragments.FeedFragment
import knk.ee.neverland.activities.fragments.GroupsFragment
import knk.ee.neverland.activities.fragments.ProfileFragment
import knk.ee.neverland.activities.fragments.QuestsFragment
import knk.ee.neverland.activities.fragments.SearchFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeBottomNavigationBar()
    }

    private fun initializeBottomNavigationBar() {
        val bottomNavigationViewEx = findViewById<BottomNavigationViewEx>(R.id.bottom_navigation)

        bottomNavigationViewEx.enableAnimation(true)
        bottomNavigationViewEx.enableItemShiftingMode(false)
        bottomNavigationViewEx.enableShiftingMode(false)
        bottomNavigationViewEx.setTextVisibility(true)

        bottomNavigationViewEx.onNavigationItemSelectedListener =
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.navigation_feed -> setMainFragment(FeedFragment())

                        R.id.navigation_quests -> setMainFragment(QuestsFragment())

                        R.id.navigation_search -> setMainFragment(SearchFragment())

                        R.id.navigation_groups -> setMainFragment(GroupsFragment())

                        R.id.navigation_profile -> setMainFragment(ProfileFragment())
                    }

                    true
                }

        bottomNavigationViewEx.currentItem = 0
    }

    private fun setMainFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_page_frame, fragment)
        fragmentTransaction.commit()
    }
}
