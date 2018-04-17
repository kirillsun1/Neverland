package knk.ee.neverland.auth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.EditText
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.models.RegistrationData
import knk.ee.neverland.utils.APIAsyncRequest
import knk.ee.neverland.utils.Utils

class RegistrationActivity : AppCompatActivity() {
    private lateinit var loginBox: AutoCompleteTextView
    private lateinit var passwordBox: EditText
    private lateinit var passwordRepeatBox: EditText
    private lateinit var firstNameBox: EditText
    private lateinit var secondNameBox: EditText
    private lateinit var emailBox: EditText
    private lateinit var agreeBox: CheckBox

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
                runRegisterTask(makeRegistrationData())
            }
        }
    }

    private fun makeRegistrationData(): RegistrationData {
        val registrationData = RegistrationData()

        registrationData.login = loginBox.text.toString()
        registrationData.password = passwordBox.text.toString()
        registrationData.firstName = firstNameBox.text.toString()
        registrationData.secondName = secondNameBox.text.toString()
        registrationData.email = emailBox.text.toString()

        return registrationData
    }

    private fun validateFields(): Boolean {
        resetErrors()

        return validateLogin() && validatePassword() && validateNames() && validateEmail()
                && checkAgreement()
    }

    private fun validateLogin(): Boolean {
        val login = loginBox.text.toString()

        if (login.isBlank()) {
            loginBox.error = getString(R.string.error_field_required)
            return false
        }

        if (!Utils.loginIsCorrect(login)) {
            loginBox.error = getString(R.string.error_invalid_login)
            return false
        }

        return true
    }

    private fun validatePassword(): Boolean {
        val password = passwordBox.text.toString()
        val repeated = passwordRepeatBox.text.toString()

        if (password.isBlank()) {
            passwordBox.error = getString(R.string.error_field_required)
            return false
        }

        if (!Utils.passwordIsCorrect(password)) {
            passwordBox.error = getString(R.string.error_invalid_password)
            return false
        }

        if (repeated.isBlank()) {
            passwordRepeatBox.error = getString(R.string.error_field_required)
            return false
        }

        if (!Utils.passwordIsCorrect(repeated)) {
            passwordRepeatBox.error = getString(R.string.error_invalid_password)
            return false
        }

        if (password != repeated) {
            passwordRepeatBox.error = getString(R.string.error_registration_password_are_not_the_same)
            return false
        }

        return true
    }

    private fun validateNames(): Boolean {
        val firstName = firstNameBox.text.toString()
        val secondName = secondNameBox.text.toString()

        if (firstName.isBlank()) {
            firstNameBox.error = getString(R.string.error_field_required)
            return false
        }

        if (!Utils.nameIsCorrect(firstName)) {
            firstNameBox.error = getString(R.string.error_invalid_first_name)
            return false
        }

        if (secondName.isBlank()) {
            secondNameBox.error = getString(R.string.error_field_required)
            return false
        }

        if (!Utils.nameIsCorrect(secondName)) {
            secondNameBox.error = getString(R.string.error_invalid_second_name)
            return false
        }

        return true
    }

    private fun validateEmail(): Boolean {
        val email = emailBox.text.toString()

        if (email.isBlank()) {
            emailBox.error = getString(R.string.error_field_required)
            return false
        }

        if (!Utils.emailIsCorrect(email)) {
            emailBox.error = getString(R.string.error_invalid_email)
            return false
        }

        return true
    }

    private fun checkAgreement(): Boolean {
        if (!agreeBox.isChecked) {
            agreeBox.error = getString(R.string.error_registration_did_not_agree)
            return false
        }

        return true
    }

    private fun resetErrors() {
        loginBox.error = null
        passwordBox.error = null
        passwordRepeatBox.error = null
        firstNameBox.error = null
        secondNameBox.error = null
        emailBox.error = null
        agreeBox.error = null
    }

    private fun runRegisterTask(registrationData: RegistrationData) {
        var success = false

        APIAsyncRequest.Builder<String>()
            .request {
                val token = DefaultAPI.authAPI.registerAccount(registrationData)
                success = true
                token
            }
            .setContext(this)
            .showMessages(true)
            .onAPIFailMessage { R.string.error_incorrect_fields }
            .after {
                if (success) {
                    finish()
                }
            }
            .finish()
            .execute()
    }
}
