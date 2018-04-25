package knk.ee.neverland.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import knk.ee.neverland.R
import knk.ee.neverland.activities.MainActivity
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.models.RegistrationData
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.UIErrorView
import knk.ee.neverland.utils.Utils

class RegistrationActivity : AppCompatActivity() {
    @BindView(R.id.registration_login)
    lateinit var loginBox: AutoCompleteTextView
    @BindView(R.id.registration_password)
    lateinit var passwordBox: EditText
    @BindView(R.id.registration_password_repeat)
    lateinit var passwordRepeatBox: EditText
    @BindView(R.id.registration_first_name)
    lateinit var firstNameBox: EditText
    @BindView(R.id.registration_second_name)
    lateinit var secondNameBox: EditText
    @BindView(R.id.registration_agree_check)
    lateinit var emailBox: EditText
    @BindView(R.id.registration_email)
    lateinit var agreeBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.registration_register)
    fun registerButtonClick() {
        if (validateFields()) {
            runRegisterTask(makeRegistrationData())
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

    private fun openMainActivityAndFinishThisActivity() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    private fun runRegisterTask(registrationData: RegistrationData) {
        var success = false

        APIAsyncTask<String>()
            .request {
                val token = DefaultAPI.authAPI.registerAccount(registrationData)
                success = true
                token
            }
            .handleResult { DefaultAPI.setUserData(registrationData.login, it) }
            .uiErrorView(UIErrorView.Builder().with(this)
                .messageOnAPIFail(R.string.error_incorrect_fields)
                .create())
            .doAfter {
                if (success) {
                    openMainActivityAndFinishThisActivity()
                }
            }
            .execute()
    }
}
