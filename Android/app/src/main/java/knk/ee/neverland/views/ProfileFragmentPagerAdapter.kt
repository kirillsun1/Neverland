package knk.ee.neverland.views

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import knk.ee.neverland.R
import knk.ee.neverland.activities.fragments.UserProofsFragment
import knk.ee.neverland.activities.fragments.UserSuggestedQuestsFragment

class ProfileFragmentPagerAdapter(val context: Context,
                                  fragmentManager: FragmentManager,
                                  private val userID: Int)
    : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        val fragment = when (position) {
            0 -> UserProofsFragment()

            1 -> UserSuggestedQuestsFragment()

            else -> throw IllegalArgumentException()
        }

        val bundle = Bundle()
        bundle.putInt("userID", userID)
        fragment.arguments = bundle

        return fragment
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.getString(R.string.completed_quests_tab_title)

            1 -> context.getString(R.string.suggested_quests_tab_title)

            else -> ""
        }
    }
}