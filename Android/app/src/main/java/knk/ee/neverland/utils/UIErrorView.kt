package knk.ee.neverland.utils

import android.content.Context
import android.widget.Toast
import knk.ee.neverland.R
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.exceptions.NetworkException

class UIErrorView private constructor(private val context: Context,
                                      private val networkErrorMessages: Map<Int, Int>,
                                      private val apiErrorMessages: Map<Int, Int>) {
    fun handleError(exception: Exception) {
        if (exception is NetworkException) {
            showErrorMessage(exception.code, networkErrorMessages)
        }

        if (exception is APIException) {
            showErrorMessage(exception.code, apiErrorMessages)
        }

        showToast(exception.message.orEmpty())
    }

    private fun showErrorMessage(code: Int, errorMessages: Map<Int, Int>) {
        if (code in errorMessages) {
            showToast(getResourceString(errorMessages[code]!!))
        } else {
            showToast(String.format(getResourceString(R.string.error_unexpected_code), code))
        }
    }

    private fun getResourceString(resID: Int): String {
        return context.getString(resID).orEmpty()
    }

    private fun showToast(message: String,
                          toastDuration: Int = Toast.LENGTH_LONG,
                          showEmpty: Boolean = false) {

        if (message.isBlank() && !showEmpty) {
            return
        }

        Toast.makeText(context, message, toastDuration).show()
    }

    class Builder {
        lateinit var context: Context

        private val networkErrorMessages: MutableMap<Int, Int> = mutableMapOf()
        private val apiErrorMessages: MutableMap<Int, Int> = mutableMapOf()

        private var showDefaultNetworkMessages: Boolean = true

        fun with(context: Context): Builder {
            this.context = context
            return this
        }

        fun messageWhenAPIError(code: Int, stringResource: Int): Builder {
            apiErrorMessages[code] = stringResource
            return this
        }

        fun messageOnAPIFail(stringResource: Int): Builder {
            return messageWhenAPIError(Constants.FAIL_CODE, stringResource)
        }

        fun messageWhenNetworkError(code: Int, stringResource: Int): Builder {
            networkErrorMessages[code] = stringResource
            return this
        }

        fun messageOnNetworkFail(stringResource: Int): Builder {
            return messageWhenNetworkError(Constants.FAIL_CODE, stringResource)
        }

        fun showDefaultNetworkMessages(value: Boolean): Builder {
            this.showDefaultNetworkMessages = value
            return this
        }

        fun create(): UIErrorView {
            if (showDefaultNetworkMessages) {
                defaultNetworkErrorMessages()
            }
            return UIErrorView(context, networkErrorMessages, apiErrorMessages)
        }

        private fun defaultNetworkErrorMessages() {
            networkErrorMessages[Constants.BAD_REQUEST_TO_API_CODE] = R.string.error_invalid_api_request
            networkErrorMessages[Constants.NETWORK_ERROR_CODE] = R.string.error_network_down
            networkErrorMessages[Constants.NETWORK_TIMEOUT] = R.string.error_slow_network
        }
    }
}
