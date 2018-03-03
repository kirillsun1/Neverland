package knk.ee.neverland.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.fragments.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeBottomNavigationBar()
        getAndSetAPIUserdata()
        openLoginActivityIfNecessary()
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

    private fun getAndSetAPIUserdata() {
        val sharedPreferences = getSharedPreferences(resources
                .getString(R.string.shared_pref_name), Context.MODE_PRIVATE)

        val login = sharedPreferences.getString(resources.getString(R.string.authkey_address), "")
        val key = sharedPreferences.getString(resources.getString(R.string.authkey_address), "")

        DefaultAPI.setUserData(login, key)
    }

    private fun openLoginActivityIfNecessary() {
        if (!DefaultAPI.isKeySet) {
            openLoginActivity()
        }
    }

    private fun openLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }
}
