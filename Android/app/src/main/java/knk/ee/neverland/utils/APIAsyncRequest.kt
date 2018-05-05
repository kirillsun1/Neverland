package knk.ee.neverland.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import knk.ee.neverland.R
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.exceptions.NetworkException

class APIAsyncRequest<Result> private constructor() {

    private var requestMethod: (() -> Result?)? = null
    private var resultMethod: ((result: Result?) -> Unit)? = null
    private var showMessages: Boolean = false
    private var context: Context? = null
    private var before: (() -> Unit)? = null
    private var after: (() -> Unit)? = null

    private var handleNetworkException: ((code: Int) -> Unit)? = null
    private var handleAPIException: ((code: Int) -> Unit)? = null

    private var apiFailMessageResource: ((code: Int) -> Int)? = null

    fun execute() {
        before?.invoke()
        CustomAsyncTask().execute()
    }

    private fun throwExceptionIfNoRequestMethod() {
        if (requestMethod == null) {
            throw Exception("No request method present")
        }
    }

    private fun throwExceptionIfNoContext() {
        if (showMessages && context == null) {
            throw Exception("Context is not set. Cannot show messages.")
        }
    }

    private fun showAPIErrorMessage(code: Int) {
        apiFailMessageResource?.invoke(code)?.let { showToast(it) }
    }

    private fun showNetworkErrorMessage(code: Int) {
        when (code) {
            Constants.BAD_REQUEST_TO_API_CODE -> showToast(R.string.error_invalid_api_request)
            Constants.NETWORK_ERROR_CODE -> showToast(R.string.error_network_down)
            Constants.NETWORK_TIMEOUT -> showToast(R.string.error_slow_network)
            else -> showToast(String.format(context!!.getString(R.string.error_unexpected_code), code))
        }
    }

    private fun showToast(messageResource: Int,
                          toastDuration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context!!, messageResource, toastDuration).show()
    }

    private fun showToast(message: String,
                          toastDuration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context!!, message, toastDuration).show()
    }

    class Builder<Result> {
        val apiAsyncRequest = knk.ee.neverland.utils.APIAsyncRequest<Result>()

        fun request(method: () -> Result?): Builder<Result> {
            apiAsyncRequest.requestMethod = method
            return this
        }

        fun handleResult(method: (result: Result?) -> Unit): Builder<Result> {
            apiAsyncRequest.resultMethod = method
            return this
        }

        fun showMessages(showMessages: Boolean): Builder<Result> {
            apiAsyncRequest.showMessages = showMessages
            return this
        }

        fun setContext(context: Context): Builder<Result> {
            apiAsyncRequest.context = context
            return this
        }

        fun before(event: () -> Unit): Builder<Result> {
            apiAsyncRequest.before = event
            return this
        }

        fun after(event: () -> Unit): Builder<Result> {
            apiAsyncRequest.after = event
            return this
        }

        fun onNetworkFail(event: (code: Int) -> Unit): Builder<Result> {
            apiAsyncRequest.handleNetworkException = event
            return this
        }

        fun onAPIFail(event: (code: Int) -> Unit): Builder<Result> {
            apiAsyncRequest.handleAPIException = event
            return this
        }

        fun onAPIFailMessage(convertCodeToResourceMethod: (code: Int) -> Int): Builder<Result> {
            apiAsyncRequest.apiFailMessageResource = convertCodeToResourceMethod
            return this
        }

        fun finish(): APIAsyncRequest<Result> {
            apiAsyncRequest.throwExceptionIfNoRequestMethod()
            apiAsyncRequest.throwExceptionIfNoContext()

            return apiAsyncRequest
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class CustomAsyncTask : AsyncTask<Void, Void, Result?>() {
        private var apiError: Int? = null
        private var networkError: Int? = null

        override fun doInBackground(vararg p0: Void?): Result? {
            return try {
                requestMethod?.invoke()
            } catch (ex: NetworkException) {
                networkError = ex.code
                return null
            } catch (ex: APIException) {
                apiError = ex.code
                return null
            }
        }

        override fun onPostExecute(result: Result?) {
            if (apiError == null && networkError == null) {
                resultMethod?.invoke(result)
            } else {
                if (apiError != null) {
                    showAPIErrorMessage(apiError!!)
                    handleAPIException?.invoke(apiError!!)
                }

                if (networkError != null) {
                    showNetworkErrorMessage(networkError!!)
                    handleNetworkException?.invoke(networkError!!)
                }
            }

            after?.invoke()
        }
    }
}