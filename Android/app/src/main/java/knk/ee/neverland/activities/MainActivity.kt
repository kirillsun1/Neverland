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
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {

    private var feedFragment: FeedFragment? = null
    private var questsFragment: QuestsFragment? = null
    private var searchFragment: SearchFragment? = null
    private var groupsFragment: GroupsFragment? = null
    private var profileFragment: ProfileFragment? = null

    private enum class Fragments {
        FEED, QUESTS, SEARCH, GROUPS, PROFILE
    }

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
                    R.id.navigation_feed -> setMainFragment(Fragments.FEED)

                    R.id.navigation_quests -> setMainFragment(Fragments.QUESTS)

                    R.id.navigation_search -> setMainFragment(Fragments.SEARCH)

                    R.id.navigation_groups -> setMainFragment(Fragments.GROUPS)

                    R.id.navigation_profile -> setMainFragment(Fragments.PROFILE)
                }

                true
            }

        bottomNavigationViewEx.currentItem = 0
    }

    private fun setMainFragment(fragment: Fragments) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_page_frame, getFragment(fragment), fragment.toString())
            .commit()
    }

    private fun getFragment(fragmentType: Fragments): Fragment {
        // TODO: find a better way to prevent fragments from recreating

        when (fragmentType) {
            Fragments.FEED -> return FeedFragment()

            Fragments.QUESTS -> return QuestsFragment()

            Fragments.SEARCH -> return SearchFragment()

            Fragments.GROUPS -> return GroupsFragment()

            Fragments.PROFILE -> return ProfileFragment()

            else -> throw IllegalArgumentException()
        }
    }
}
