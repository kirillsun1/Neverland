package knk.ee.neverland.activities

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.User
import knk.ee.neverland.utils.APIAsyncRequest
import knk.ee.neverland.utils.Utils
import knk.ee.neverland.views.HeightWrappingViewPager
import knk.ee.neverland.views.ProfileFragmentPagerAdapter

class ProfileActivity : AppCompatActivity() {

    private val SELF_ID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(findViewById(R.id.profile_toolbar))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val userID = getUserIDFromIntent()
        changeFollowButtonProperties(userID)
        runLoadUserDataTask(userID)
    }

    private fun getUserIDFromIntent(): Int {
        if (intent.extras == null) {
            return SELF_ID
        }

        val value = intent.extras.getInt("userID")

        return if (value != 0) value else SELF_ID
    }

    private fun setUserData(user: User) {
        //findViewById<TextView>(R.id.profile_first_second_name).text = user.toString()
        //findViewById<TextView>(R.id.profile_user_name).text = user.userName

        findViewById<CollapsingToolbarLayout>(R.id.profile_collapsing_toolbar).title = user.toString()

        val ratingString = "%.0f%%".format(user.rating * 100)
        findViewById<TextView>(R.id.profile_user_rating).text = ratingString
        findViewById<TextView>(R.id.profile_user_following).text = Utils.compactHugeNumber(user.following)
        findViewById<TextView>(R.id.profile_user_followers).text = Utils.compactHugeNumber(user.followers)

        Glide.with(this)
            .load(user.avatarLink)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .transition(DrawableTransitionOptions.withCrossFade(resources.getInteger(
                R.integer.feed_fade_animation_duration)))
            .into(findViewById(R.id.profile_user_avatar))

        initializeViewPager(user.id)
    }

    private fun initializeViewPager(userID: Int) {
        val viewPager = findViewById<HeightWrappingViewPager>(R.id.profile_current_tab)
        viewPager.adapter = ProfileFragmentPagerAdapter(this, supportFragmentManager, userID)

        findViewById<TabLayout>(R.id.profile_tabs).setupWithViewPager(viewPager)
    }

    private fun changeFollowButtonProperties(userID: Int) {
        val followButton = findViewById<Button>(R.id.profile_follow_button)

        followButton.visibility = if (userID == SELF_ID) GONE else VISIBLE
        followButton.isEnabled = userID != SELF_ID
    }

    private fun runLoadUserDataTask(userID: Int) {
        APIAsyncRequest.Builder<User>()
            .request {
                if (userID == SELF_ID)
                    DefaultAPI.userAPI.getMyData()
                else
                    DefaultAPI.userAPI.getUserData(userID)
            }
            .handleResult { setUserData(it!!) }
            .onAPIFailMessage { R.string.error_getting_user_data }
            .setContext(this)
            .showMessages(true)
            .finish()
            .execute()
    }
}
