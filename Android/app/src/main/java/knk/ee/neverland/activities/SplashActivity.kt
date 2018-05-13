package knk.ee.neverland.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import knk.ee.neverland.R
import knk.ee.neverland.auth.LoginActivity
import knk.ee.neverland.auth.TokenChecker

class SplashActivity : AppCompatActivity() {

    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar

    @BindView(R.id.errorLabel)
    lateinit var errorLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        ButterKnife.bind(this)

        TokenChecker(this)
            .doIfTokenIsActive { openMainActivity() }
            .doIfTokenIsNotActive { openLoginActivity() }
            .doIfNoInternet {
                showErrorMessage(true)
            }
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

    private fun showErrorMessage(error: Boolean) {
        errorLabel.visibility = if (error) VISIBLE else GONE
        progressBar.visibility = if (error) GONE else VISIBLE
    }
}
