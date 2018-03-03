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
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.exceptions.AuthAPIException
import knk.ee.neverland.pojos.RegistrationData

class RegistrationActivity : AppCompatActivity() {
    private var loginBox: AutoCompleteTextView? = null
    private var passwordBox: EditText? = null
    private var firstNameBox: EditText? = null
    private var secondNameBox: EditText? = null
    private var emailBox: EditText? = null

    private var registerTask: RegisterTask? = null
    private var loginTask: LoginTask? = null

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

    @Throws(AuthAPIException::class)
    private fun login(login: String, pass: String) {
        loginTask = LoginTask(login, pass)
        loginTask!!.execute()
    }

    private fun showFailMessage(message: String) {
        val context = applicationContext
        val duration = Toast.LENGTH_LONG

        val toast = Toast.makeText(context, message, duration)
        toast.show()
    }

    private fun finishNow(key: String) {
        val intent = Intent()
        intent.putExtra("key", key)
        setResult(SUCCESSFUL_REGISTRATION, intent)
        finish()
    }

    private fun validateFields(): Boolean {
        if (loginBox!!.text.isBlank()) {
            loginBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (passwordBox!!.text.isBlank()) {
            passwordBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (firstNameBox!!.text.isBlank()) {
            firstNameBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (secondNameBox!!.text.isBlank()) {
            secondNameBox!!.error = getString(R.string.error_field_required)
            return false
        }

        if (emailBox!!.text.isBlank()) {
            emailBox!!.error = getString(R.string.error_field_required)
            return false
        }

        return true
    }

    companion object {
        val SUCCESSFUL_REGISTRATION = 1
    }

    @SuppressLint("StaticFieldLeak")
    private inner class RegisterTask(val registrationData: RegistrationData) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg p0: Void?): Boolean {
            DefaultAPI.authAPI.registerAccount(registrationData)
            return true // TODO: catch exceptions
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                login(registrationData.login, registrationData.password)
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class LoginTask(val login: String, val pass: String) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            return DefaultAPI.authAPI.attemptLogin(login, pass)
        }

        override fun onPostExecute(result: String?) {
            finishNow(result!!)
        }
    }
}
