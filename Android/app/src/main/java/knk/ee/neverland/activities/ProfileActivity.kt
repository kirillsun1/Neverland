package knk.ee.neverland.activities

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

class ProfileActivity : AppCompatActivity() {

    val SELF_ID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val idToLoad = getUserIDFromIntent()
        LoadUserDataTask().execute(idToLoad)
    }

    private fun getUserIDFromIntent(default: Int = SELF_ID): Int {
        if (intent.extras == null) {
            return default
        }

        val value = intent.extras.getInt("userID")

        return if (value != 0) value else default
    }

    private fun setUserData(user: User) {
        findViewById<TextView>(R.id.profile_first_second_name).text = user.toString()
        findViewById<TextView>(R.id.profile_user_name).text = user.userName

        // TODO: rating stuff

        Glide.with(this)
            .load(user.avatarLink)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .transition(DrawableTransitionOptions.withCrossFade(resources.getInteger(
                R.integer.feed_fade_animation_duration)))
            .into(findViewById(R.id.profile_user_avatar))
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
