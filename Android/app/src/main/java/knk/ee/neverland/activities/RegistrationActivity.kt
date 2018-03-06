package knk.ee.neverland.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import knk.ee.neverland.R
import knk.ee.neverland.api.Constants
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.exceptions.AuthAPIException
import knk.ee.neverland.models.RegistrationData

class RegistrationActivity : AppCompatActivity() {
    private var loginBox: AutoCompleteTextView? = null
    private var passwordBox: EditText? = null
    private var firstNameBox: EditText? = null
    private var secondNameBox: EditText? = null
    private var emailBox: EditText? = null
    private var registerTask: RegisterTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        loginBox = findViewById(R.id.registration_login)
        passwordBox = findViewById(R.id.registration_password)
        firstNameBox = findViewById(R.id.registration_first_name)
        secondNameBox = findViewById(R.id.registration_second_name)
        emailBox = findViewById(R.id.registration_email)

        findViewById<View>(R.id.registration_register).setOnClickListener {
            if (validateFields()) {
                register(makeRegistrationData())
            }
        }
    }

    private fun makeRegistrationData(): RegistrationData {
        val registrationData = RegistrationData()

        registrationData.login = loginBox!!.text.toString()
        registrationData.password = passwordBox!!.text.toString()
        registrationData.firstName = firstNameBox!!.text.toString()
        registrationData.secondName = secondNameBox!!.text.toString()
        registrationData.email = emailBox!!.text.toString()

        return registrationData
    }

    @Throws(AuthAPIException::class)
    private fun register(registrationData: RegistrationData) {
        registerTask = RegisterTask(registrationData)
        registerTask!!.execute()
    }

    private fun showToast(message: String) {
        val context = applicationContext
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(context, message, duration)
        toast.show()
    }

    private fun finishNow(login: String, token: String) {
        val intent = Intent()
        intent.putExtra("token", token)
        intent.putExtra("login", login)
        setResult(Constants.SUCCESS, intent)
        finish()
    }

    private fun validateFields(): Boolean {
        if (loginBox!!.text.isBlank()) {
            loginBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!loginIsCorrect(loginBox!!.text.toString())) {
            loginBox!!.error = getString(R.string.error_invalid_login)
            return false
        }

        if (passwordBox!!.text.isBlank()) {
            passwordBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!passwordIsCorrect(passwordBox!!.text.toString())) {
            passwordBox!!.error = getString(R.string.error_invalid_password)
            return false
        }

        if (firstNameBox!!.text.isBlank()) {
            firstNameBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!nameIsCorrect(firstNameBox!!.text.toString())) {
            firstNameBox!!.error = getString(R.string.error_invalid_first_name)
            return false
        }

        if (secondNameBox!!.text.isBlank()) {
            secondNameBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!nameIsCorrect(secondNameBox!!.text.toString())) {
            secondNameBox!!.error = getString(R.string.error_invalid_second_name)
            return false
        }

        if (emailBox!!.text.isBlank()) {
            emailBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!emailIsCorrect(emailBox!!.text.toString())) {
            emailBox!!.error = getString(R.string.error_invalid_email)
            return false
        }

        return true
    }

    private fun loginIsCorrect(login: String): Boolean = login.matches(Constants.LOGIN_REGEX)

    private fun passwordIsCorrect(password: String): Boolean
            = password.matches(Constants.PASSWORD_REGEX)

    private fun emailIsCorrect(email: String): Boolean = email.matches(Constants.EMAIL_REGEX)

    private fun nameIsCorrect(name: String): Boolean = name.matches(Constants.NAME_REGEX)

    @SuppressLint("StaticFieldLeak")
    private inner class RegisterTask(val registrationData: RegistrationData) : AsyncTask<Void, Void, Int>() {
        private var token: String = ""

        override fun doInBackground(vararg p0: Void?): Int {
            try {
                token = DefaultAPI.authAPI.registerAccount(registrationData)
                return Constants.SUCCESS
            } catch (ex: AuthAPIException) {
                return ex.code
            }
        }

        override fun onPostExecute(code: Int?) {
            when (code) {
                Constants.BAD_REQUEST_TO_API -> showToast(getString(R.string.error_invalid_api_request))
                Constants.NETWORK_ERROR -> showToast(getString(R.string.error_network_down))
                Constants.FAILED -> showToast(getString(R.string.error_incorrect_fields))
                Constants.SUCCESS -> finishNow(registrationData.login, token)
                else -> showToast(String.format("%s %d", getString(R.string.error_unexpected_code), code))
            }
        }
    }
}
