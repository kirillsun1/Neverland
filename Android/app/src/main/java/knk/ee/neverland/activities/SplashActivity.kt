package knk.ee.neverland.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.auth.LoginActivity
import knk.ee.neverland.utils.APIAsyncRequest
import java.util.concurrent.atomic.AtomicBoolean

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setUserDataFromSystemPreferences()

        if (DefaultAPI.isKeySet()) {
            runCheckTokenTask(DefaultAPI.userToken!!)
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
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun runCheckTokenTask(userToken: String) {
        val tokenIsOK = AtomicBoolean(false)

        APIAsyncRequest.Builder<Boolean>()
            .request {
                tokenIsOK.set(DefaultAPI.authAPI.isTokenActive(userToken))
                true
            }
            .setContext(this)
            .showMessages(true)
            .after {
                if (!tokenIsOK.get()) {
                    openLoginActivity()
                    showToast(getString(R.string.error_invalid_token))
                } else {
                    openMainActivity()
                }
            }
            .onNetworkFail { openLoginActivity() }
            .finish()
            .execute()
    }
}
