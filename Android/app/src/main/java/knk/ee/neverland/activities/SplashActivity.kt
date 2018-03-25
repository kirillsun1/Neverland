package knk.ee.neverland.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.exceptions.NetworkException

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setUserDataFromSystemPreferences()

        if (DefaultAPI.isKeySet()) {
            CheckTokenTask(DefaultAPI.userToken!!).execute()
        } else {
            openLoginActivity()
        }
    }

    private fun setUserDataFromSystemPreferences() {
        val sharedPreferences = getSharedPreferences(resources
            .getString(R.string.shared_pref_name), Context.MODE_PRIVATE)

        val login = sharedPreferences.getString(resources.getString(R.string.auth_login_address), "")
        val key = sharedPreferences.getString(resources.getString(R.string.auth_key_address), "")

        DefaultAPI.setUserData(login, key)
    }

    private fun openLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    private fun openMainActivity() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    private fun showToast(message: String) {
        val context = applicationContext
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(context, message, duration)
        toast.show()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class CheckTokenTask(val token: String) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg p0: Void?): Boolean {
            try {
                return DefaultAPI.authAPI.isTokenActive(token)
            } catch (_: APIException) {
                return false
            } catch (_: NetworkException) {
                return false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            if (!result!!) {
                openLoginActivity()
                showToast(getString(R.string.error_invalid_token))
            } else {
                openMainActivity()
            }
        }
    }
}
