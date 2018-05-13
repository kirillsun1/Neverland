package knk.ee.neverland.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import knk.ee.neverland.R
import knk.ee.neverland.feed.FeedFragment
import knk.ee.neverland.groups.GroupsFragment
import knk.ee.neverland.profile.ProfileActivity
import knk.ee.neverland.quests.TakenQuestsFragment
import knk.ee.neverland.utils.Constants

class MainActivity : AppCompatActivity() {
    enum class FragmentType {
        FEED, QUESTS, GROUPS
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeBottomNavigationBar()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.SUBMIT_NEW_QUEST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            showToast("Quest created")
        }
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
                        R.id.navigation_feed -> setMainFragment(FragmentType.FEED)

                        R.id.navigation_quests -> setMainFragment(FragmentType.QUESTS)

                        R.id.navigation_groups -> setMainFragment(FragmentType.GROUPS)

                        R.id.navigation_profile -> openUserProfile()
                    }

                    true
                }

        bottomNavigationViewEx.currentItem = 0
    }

    private fun openUserProfile() {
        startActivity(Intent(applicationContext, ProfileActivity::class.java))
    }

    private fun setMainFragment(fragmentType: FragmentType) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        var fragmentObject = fragmentManager.findFragmentByTag(fragmentType.toString())

        if (fragmentObject == null) {
            fragmentObject = createNewFragmentObject(fragmentType)
        }

        fragmentTransaction.replace(R.id.main_page_frame, fragmentObject, fragmentType.toString())
        fragmentTransaction.addToBackStack(fragmentType.toString())
        fragmentTransaction.commit()
    }

    private fun createNewFragmentObject(fragmentType: FragmentType): Fragment {
        return when (fragmentType) {
            FragmentType.FEED -> FeedFragment()

            FragmentType.QUESTS -> TakenQuestsFragment()

            FragmentType.GROUPS -> GroupsFragment()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
