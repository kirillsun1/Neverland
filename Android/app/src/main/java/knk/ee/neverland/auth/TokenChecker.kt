package knk.ee.neverland.auth

import android.content.Context
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.exceptions.NetworkException
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.UIErrorView

typealias ActionWhenTokenChecked = () -> Unit

class TokenChecker(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences(context.resources
        .getString(R.string.shared_pref_name), Context.MODE_PRIVATE)!!

    private lateinit var actionIfTokenIsNotActive: ActionWhenTokenChecked
    private lateinit var actionIfTokenIsActive: ActionWhenTokenChecked
    private lateinit var actionIfNoInternet: ActionWhenTokenChecked

    private val tokenPath = context.resources.getString(R.string.auth_key_address)
    private val userNamePath = context.resources.getString(R.string.auth_login_address)

    fun doIfTokenIsNotActive(method: ActionWhenTokenChecked): TokenChecker {
        this.actionIfTokenIsNotActive = method
        return this
    }

    fun doIfTokenIsActive(method: ActionWhenTokenChecked): TokenChecker {
        this.actionIfTokenIsActive = method
        return this
    }

    fun doIfNoInternet(method: ActionWhenTokenChecked): TokenChecker {
        this.actionIfNoInternet = method
        return this
    }

    fun checkToken() {
        val token = getTokenFromSharedPreferences()
        val userName = getUserNameFromSharedPreferences()

        if (token.isBlank() || userName.isBlank()) {
            fail()
            return
        }

        APIAsyncTask<Int>()
            .request {
                DefaultAPI.authAPI.getUserID(token)
            }
            .handleResult { userID ->
                DefaultAPI.setUserData(userName, token, userID)
                actionIfTokenIsActive.invoke()
            }
            .onError { error ->
                if (error is APIException) {
                    fail()
                }

                if (error is NetworkException) {
                    failWithNetwork()
                }
            }
            .uiErrorView(UIErrorView.Builder()
                .with(context)
                .create())
            .execute()
    }

    private fun fail() {
        eraseTokenAndUserNameFromSharedPreferences()
        actionIfTokenIsNotActive.invoke()
    }

    private fun failWithNetwork() {
        actionIfNoInternet.invoke()
    }

    private fun eraseTokenAndUserNameFromSharedPreferences() {
        val editor = sharedPreferences.edit()

        editor.remove(tokenPath)
        editor.remove(userNamePath)

        editor.apply()
    }

    private fun getUserNameFromSharedPreferences(): String =
        sharedPreferences.getString(userNamePath, "")

    private fun getTokenFromSharedPreferences(): String =
        sharedPreferences.getString(tokenPath, "")
}