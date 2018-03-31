package knk.ee.neverland.activities

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.exceptions.NetworkException
import knk.ee.neverland.models.User
import knk.ee.neverland.utils.Constants
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
        LoadUserDataTask().execute(userID)
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

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class LoadUserDataTask : AsyncTask<Int, Void, User?>() {
        private var networkErrorCode: Int? = null
        private var apiErrorCode: Int? = null

        override fun doInBackground(vararg args: Int?): User? {
            try {
                val id = args[0]!!

                if (id == SELF_ID)
                    return DefaultAPI.userAPI.getMyData()
                else
                    return DefaultAPI.userAPI.getUserData(id)
            } catch (ex: NetworkException) {
                networkErrorCode = ex.code
                return null
            } catch (ex: APIException) {
                apiErrorCode = ex.code
                return null
            }
        }

        override fun onPostExecute(result: User?) {
            if (result != null) {
                setUserData(result)
            } else {
                if (networkErrorCode != null) {
                    failWithNetworkException(networkErrorCode!!)
                }

                if (apiErrorCode != null) {
                    failWithAPIException(apiErrorCode!!)
                }
            }
        }

        private fun failWithAPIException(code: Int) {
            when (code) {
                Constants.FAIL_CODE -> showMessage(getString(R.string.error_getting_proofs_failed))
                else -> showMessage(String.format(getString(R.string.error_unexpected_code), code))
            }
        }

        private fun failWithNetworkException(code: Int) {
            when (code) {
                Constants.BAD_REQUEST_TO_API_CODE -> showMessage(getString(R.string.error_invalid_api_request))
                Constants.NETWORK_ERROR_CODE -> showMessage(getString(R.string.error_network_down))
                Constants.NETWORK_TIMEOUT -> showMessage(getString(R.string.error_slow_network))
                else -> showMessage(String.format(getString(R.string.error_unexpected_code), code))
            }
        }
    }
}
