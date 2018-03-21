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
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.exceptions.AuthAPIException
import knk.ee.neverland.exceptions.NetworkException
import knk.ee.neverland.utils.Constants
import knk.ee.neverland.utils.Utils

class LoginActivity : AppCompatActivity() {
    private var mAuthTask: UserLoginTask? = null
    // UI references.
    private var mLoginView: AutoCompleteTextView? = null
    private var mPasswordView: EditText? = null
    private var mProgressView: View? = null
    private var mLoginFormView: View? = null

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Constants.SUCCESS_CODE) {
            val token = data!!.getStringExtra("token")
            val login = data.getStringExtra("login")
            saveUserdataToTheSystemSettings(login, token)
            openMainActivityAndFinishThisActivity()
        }
    }

    override fun onBackPressed() {
        System.exit(0)
    }

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
        if (!TextUtils.isEmpty(password) && !Utils.passwordIsCorrect(password)) {
            mPasswordView!!.error = getString(R.string.error_invalid_password)
            focusView = mPasswordView
            cancel = true
        }
        // Check for a valid login address.
        if (TextUtils.isEmpty(login)) {
            mLoginView!!.error = getString(R.string.error_field_required)
            focusView = mLoginView
            cancel = true
        } else if (!Utils.loginIsCorrect(login)) {
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
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
    }

    private fun saveUserdataToTheSystemSettings(login: String, token: String) {
        DefaultAPI.setUserData(login, token)

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

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class UserLoginTask internal constructor(private val mLogin: String, private val mPassword: String) : AsyncTask<Void, Void, Int>() {

        override fun doInBackground(vararg params: Void): Int? {
            try {
                saveUserdataToTheSystemSettings(mLogin,
                    DefaultAPI.authAPI.attemptLogin(mLogin, mPassword))
                return Constants.SUCCESS_CODE
            } catch (ex: AuthAPIException) {
                return ex.code
            } catch (ex: NetworkException) {
                return ex.code
            }
        }

        override fun onPostExecute(code: Int?) {
            mAuthTask = null
            showProgress(false)

            when (code) {
                Constants.BAD_REQUEST_TO_API_CODE -> showToast(getString(R.string.error_invalid_api_request))
                Constants.NETWORK_ERROR_CODE -> showToast(getString(R.string.error_network_down))
                Constants.FAIL_CODE -> showToast(getString(R.string.error_incorrect_field))
                Constants.SUCCESS_CODE -> openMainActivityAndFinishThisActivity()
                else -> showToast(String.format(getString(R.string.error_unexpected_code), code))
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }
}

