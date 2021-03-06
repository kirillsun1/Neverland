package knk.ee.neverland.auth

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import knk.ee.neverland.R
import knk.ee.neverland.activities.MainActivity
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.Constants
import knk.ee.neverland.utils.UIErrorView
import knk.ee.neverland.utils.Utils

class LoginActivity : AppCompatActivity() {

    @BindView(R.id.registration_login)
    lateinit var mLoginView: AutoCompleteTextView
    @BindView(R.id.registration_password)
    lateinit var mPasswordView: EditText
    @BindView(R.id.login_progress)
    lateinit var mProgressView: View
    @BindView(R.id.login_form)
    lateinit var mLoginFormView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        mPasswordView.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Constants.SUCCESS_CODE) {
            val token = data!!.getStringExtra("token")
            val login = data.getStringExtra("login")
            val userID = data.getIntExtra("uid", Int.MIN_VALUE)
            saveUserdataToTheSystemSettings(login, token, userID)
            openMainActivityAndFinishThisActivity()
        }
    }

    override fun onBackPressed() {
        System.exit(0)
    }

    @OnClick(R.id.email_sign_in_button)
    fun signInButtonClick() {
        attemptLogin()
    }

    @OnClick(R.id.login_register)
    fun registerButtonClick() {
        startActivityForResult(Intent(this, RegistrationActivity::class.java), 1)
    }

    private fun attemptLogin() {
        // Reset errors.
        mLoginView.error = null
        mPasswordView.error = null
        // Store values at the time of the login attempt.
        val login = mLoginView.text.toString()
        val password = mPasswordView.text.toString()
        var cancel = false
        var focusView: View? = null
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !Utils.passwordIsCorrect(password)) {
            mPasswordView.error = getString(R.string.error_invalid_password)
            focusView = mPasswordView
            cancel = true
        }
        // Check for a valid login address.
        if (TextUtils.isEmpty(login)) {
            mLoginView.error = getString(R.string.error_field_required)
            focusView = mLoginView
            cancel = true
        } else if (!Utils.loginIsCorrect(login)) {
            mLoginView.error = getString(R.string.error_invalid_login)
            focusView = mLoginView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            runUserLoginTask(login, password)
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        mLoginFormView.visibility = if (show) View.GONE else View.VISIBLE
        mLoginFormView.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mLoginFormView.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        mProgressView.visibility = if (show) View.VISIBLE else View.GONE
        mProgressView.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mProgressView.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    private fun saveUserdataToTheSystemSettings(login: String, token: String, userId: Int) {
        DefaultAPI.setUserData(login, token, userId)

        val sharedPreferences = getSharedPreferences(resources.getString(R.string.shared_pref_name),
            Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(resources.getString(R.string.auth_key_address), token)
        editor.putString(resources.getString(R.string.auth_login_address), login)
        editor.apply()
    }

    private fun openMainActivityAndFinishThisActivity() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    private fun runUserLoginTask(login: String, password: String) {
        APIAsyncTask<Pair<String, Int>>()
            .doBefore { showProgress(true) }
            .request { DefaultAPI.authAPI.attemptLogin(login, password) }
            .handleResult {
                saveUserdataToTheSystemSettings(login, it.first, it.second)
                openMainActivityAndFinishThisActivity()
            }
            .uiErrorView(UIErrorView.Builder().with(this)
                .messageOnAPIFail(R.string.error_incorrect_field)
                .create())
            .doAfter { showProgress(false) }
            .execute()
    }
}

