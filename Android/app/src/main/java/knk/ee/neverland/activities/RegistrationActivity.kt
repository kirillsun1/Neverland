package knk.ee.neverland.activities

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.models.RegistrationData
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.exceptions.NetworkException
import knk.ee.neverland.utils.Constants
import knk.ee.neverland.utils.Utils

class RegistrationActivity : AppCompatActivity() {
    private var loginBox: AutoCompleteTextView? = null
    private var passwordBox: EditText? = null
    private var passwordRepeatBox: EditText? = null
    private var firstNameBox: EditText? = null
    private var secondNameBox: EditText? = null
    private var emailBox: EditText? = null
    private var agreeBox: CheckBox? = null

    private var registerTask: RegisterTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        loginBox = findViewById(R.id.registration_login)
        passwordBox = findViewById(R.id.registration_password)
        passwordRepeatBox = findViewById(R.id.registration_password_repeat)
        firstNameBox = findViewById(R.id.registration_first_name)
        secondNameBox = findViewById(R.id.registration_second_name)
        agreeBox = findViewById(R.id.registration_agree_check)
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

    @Throws(APIException::class)
    private fun register(registrationData: RegistrationData) {
        registerTask = RegisterTask(registrationData)
        registerTask!!.execute()
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun validateFields(): Boolean {
        resetErrors()

        return validateLogin() && validatePassword() && validateNames() && validateEmail()
            && checkAgreement()
    }

    private fun validateLogin(): Boolean {
        val login = loginBox!!.text.toString()

        if (login.isBlank()) {
            loginBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!Utils.loginIsCorrect(login)) {
            loginBox!!.error = getString(R.string.error_invalid_login)
            return false
        }

        return true
    }

    private fun validatePassword(): Boolean {
        val password = passwordBox!!.text.toString()
        val repeated = passwordRepeatBox!!.text.toString()

        if (password.isBlank()) {
            passwordBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!Utils.passwordIsCorrect(password)) {
            passwordBox!!.error = getString(R.string.error_invalid_password)
            return false
        }

        if (repeated.isBlank()) {
            passwordRepeatBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!Utils.passwordIsCorrect(repeated)) {
            passwordRepeatBox!!.error = getString(R.string.error_invalid_password)
            return false
        }

        if (password != repeated) {
            passwordRepeatBox!!.error = getString(R.string.error_registration_password_are_not_the_same)
            return false
        }

        return true
    }

    private fun validateNames(): Boolean {
        val firstName = firstNameBox!!.text.toString()
        val secondName = secondNameBox!!.text.toString()

        if (firstName.isBlank()) {
            firstNameBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!Utils.nameIsCorrect(firstName)) {
            firstNameBox!!.error = getString(R.string.error_invalid_first_name)
            return false
        }

        if (secondName.isBlank()) {
            secondNameBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!Utils.nameIsCorrect(secondName)) {
            secondNameBox!!.error = getString(R.string.error_invalid_second_name)
            return false
        }

        return true
    }

    private fun validateEmail(): Boolean {
        val email = emailBox!!.text.toString()

        if (email.isBlank()) {
            emailBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!Utils.emailIsCorrect(email)) {
            emailBox!!.error = getString(R.string.error_invalid_email)
            return false
        }

        return true
    }

    private fun checkAgreement(): Boolean {
        if (!agreeBox!!.isChecked) {
            agreeBox!!.error = getString(R.string.error_registration_did_not_agree)
            return false
        }

        return true
    }

    private fun resetErrors() {
        loginBox!!.error = null
        passwordBox!!.error = null
        passwordRepeatBox!!.error = null
        firstNameBox!!.error = null
        secondNameBox!!.error = null
        emailBox!!.error = null
        agreeBox!!.error = null
    }

    @SuppressLint("StaticFieldLeak")
    private inner class RegisterTask(val registrationData: RegistrationData) : AsyncTask<Void, Void, Int>() {

        private var token: String = ""

        override fun doInBackground(vararg p0: Void?): Int {
            try {
                token = DefaultAPI.authAPI.registerAccount(registrationData)
                return Constants.SUCCESS_CODE
            } catch (ex: APIException) {
                return ex.code
            } catch (ex: NetworkException) {
                return ex.code
            }
        }

        override fun onPostExecute(code: Int?) {
            when (code) {
                Constants.BAD_REQUEST_TO_API_CODE -> showToast(getString(R.string.error_invalid_api_request))
                Constants.NETWORK_ERROR_CODE -> showToast(getString(R.string.error_network_down))
                Constants.FAIL_CODE -> showToast(getString(R.string.error_incorrect_fields))
                Constants.SUCCESS_CODE -> finish()
                else -> showToast(String.format(getString(R.string.error_unexpected_code), code))
            }
        }
    }
}
