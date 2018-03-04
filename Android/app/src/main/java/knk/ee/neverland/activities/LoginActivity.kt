package knk.ee.neverland.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import knk.ee.neverland.R
import knk.ee.neverland.api.AuthAPIConstants
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.exceptions.AuthAPIException

class LoginActivity : AppCompatActivity() {
    private var mAuthTask: UserLoginTask? = null
    // UI references.
    private var mLoginView: AutoCompleteTextView? = null
    private var mPasswordView: EditText? = null
    private var mProgressView: View? = null
    private var mLoginFormView: View? = null

    override fun onBackPressed() {
        System.exit(0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Set up the login form.
        mLoginView = findViewById(R.id.registration_login)

        mPasswordView = findViewById(R.id.registration_password)
        mPasswordView!!.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
        val mEmailSignInButton = findViewById<Button>(R.id.email_sign_in_button)
        mEmailSignInButton.setOnClickListener { attemptLogin() }

        mLoginFormView = findViewById(R.id.login_form)
        mProgressView = findViewById(R.id.login_progress)

        findViewById<View>(R.id.login_register).setOnClickListener { view -> startActivityForResult(Intent(view.context, RegistrationActivity::class.java), 1) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == AuthAPIConstants.SUCCESS) {
            val token = data.getStringExtra("token")
            val login = data.getStringExtra("login")
            saveUserdataToTheSystemSettings(login, token)
            openMainActivityAndFinishThisActivity()
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }
        // Reset errors.
        mLoginView!!.error = null
        mPasswordView!!.error = null
        // Store values at the time of the login attempt.
        val login = mLoginView!!.text.toString()
        val password = mPasswordView!!.text.toString()
        var cancel = false
        var focusView: View? = null
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView!!.error = getString(R.string.error_invalid_password)
            focusView = mPasswordView
            cancel = true
        }
        // Check for a valid login address.
        if (TextUtils.isEmpty(login)) {
            mLoginView!!.error = getString(R.string.error_field_required)
            focusView = mLoginView
            cancel = true
        } else if (!isLoginValid(login)) {
            mLoginView!!.error = getString(R.string.error_invalid_login)
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
            showProgress(true)
            mAuthTask = UserLoginTask(login, password)
            mAuthTask!!.execute(null as Void?)
        }
    }

    private fun isLoginValid(login: String): Boolean {
        return login.matches(AuthAPIConstants.LOGIN_REGEX)
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.matches(AuthAPIConstants.PASSWORD_REGEX)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

            mLoginFormView!!.visibility = if (show) View.GONE else View.VISIBLE
            mLoginFormView!!.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mLoginFormView!!.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

            mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
            mProgressView!!.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
            mLoginFormView!!.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    private fun saveUserdataToTheSystemSettings(login: String, token: String) {
        DefaultAPI.setUserData(login, token)

        val sharedPreferences = getSharedPreferences(resources.getString(R.string.shared_pref_name),
                Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(resources.getString(R.string.authkey_address), token)
        editor.putString(resources.getString(R.string.authlogin_address), login)
        editor.apply()
    }

    private fun openMainActivityAndFinishThisActivity() {
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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("StaticFieldLeak")
    private inner class UserLoginTask internal constructor(private val mLogin: String, private val mPassword: String) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void): Int? {
            try {
                saveUserdataToTheSystemSettings(mLogin,
                        DefaultAPI.authAPI.attemptLogin(mLogin, mPassword))
                return AuthAPIConstants.SUCCESS
            } catch (e: AuthAPIException) {
                return e.code
            }
        }

        override fun onPostExecute(code: Int?) {
            mAuthTask = null
            showProgress(false)

            when (code) {
                AuthAPIConstants.BAD_REQUEST_TO_API -> showToast(getString(R.string.error_invalid_api_request))
                AuthAPIConstants.NETWORK_ERROR -> showToast(getString(R.string.error_network_down))
                AuthAPIConstants.FAILED -> showToast(getString(R.string.error_incorrect_field))
                AuthAPIConstants.SUCCESS -> openMainActivityAndFinishThisActivity()
                else -> showToast(String.format("%s %d", getString(R.string.error_unexpected_code), code))
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }
}

