package knk.ee.neverland.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import knk.ee.neverland.R
import knk.ee.neverland.auth.LoginActivity
import knk.ee.neverland.auth.TokenChecker

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        TokenChecker(this)
            .doIfTokenIsActive { openMainActivity() }
            .doIfTokenIsNotActive { openLoginActivity() }
            .checkToken()
    }

    private fun openLoginActivity() {
        val loginIntent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    private fun openMainActivity() {
        val mainIntent = Intent(applicationContext, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }
}
