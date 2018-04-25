package knk.ee.neverland.network

import android.os.AsyncTask
import knk.ee.neverland.utils.UIErrorView

typealias SimpleMethod = () -> Unit
typealias OnErrorMethod = (Exception) -> Unit

class APIAsyncTask<Result> {
    private var doBeforeMethod: SimpleMethod? = null
    private var doAfterMethod: SimpleMethod? = null
    private var onErrorMethod: OnErrorMethod? = null

    private var uiErrorView: UIErrorView? = null

    private lateinit var requestMethod: () -> Result
    private var resultHandlerMethod: ((Result) -> Unit)? = null

    fun doBefore(doBeforeMethod: SimpleMethod): APIAsyncTask<Result> {
        this.doBeforeMethod = doBeforeMethod
        return this
    }

    fun request(requestMethod: () -> Result): APIAsyncTask<Result> {
        this.requestMethod = requestMethod
        return this
    }

    fun handleResult(resultHandlerMethod: (Result) -> Unit): APIAsyncTask<Result> {
        this.resultHandlerMethod = resultHandlerMethod
        return this
    }

    fun onError(onErrorMethod: OnErrorMethod): APIAsyncTask<Result> {
        this.onErrorMethod = onErrorMethod
        return this
    }

    fun uiErrorView(uiErrorView: UIErrorView): APIAsyncTask<Result> {
        this.uiErrorView = uiErrorView
        return this
    }

    fun doAfter(doAfterMethod: SimpleMethod): APIAsyncTask<Result> {
        this.doAfterMethod = doAfterMethod
        return this
    }

    fun execute() {
        doBeforeMethod?.invoke()
        APITask(requestMethod,
            resultHandlerMethod,
            onErrorMethod,
            doAfterMethod,
            uiErrorView).execute()
    }

    private class APITask<Result>(private val requestMethod: () -> Result,
                                  private val resultHandlerMethod: ((Result) -> Unit)?,
                                  private val onErrorMethod: OnErrorMethod?,
                                  private val doAfterMethod: SimpleMethod?,
                                  private val uiErrorView: UIErrorView?)
        : AsyncTask<Void, Void, Result>() {

        private var catchedException: Exception? = null

        override fun doInBackground(vararg args: Void?): Result? {
            try {
                return requestMethod.invoke()
            } catch (ex: Exception) {
                catchedException = ex
                return null
            }
        }

        override fun onPostExecute(result: Result?) {
            if (catchedException != null) {
                onErrorMethod?.invoke(catchedException!!)
                uiErrorView?.handleError(catchedException!!)
            } else {
                resultHandlerMethod?.invoke(result!!)
            }

            doAfterMethod?.invoke()
        }
    }
}