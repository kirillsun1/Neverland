package knk.ee.neverland.network

import android.os.AsyncTask
import knk.ee.neverland.utils.UIErrorView

typealias SimpleMethod = () -> Unit
typealias OnErrorMethod = (Exception) -> Unit
typealias RequestMethod<T> = () -> T
typealias HandleResultMethod<T> = (T) -> Unit

class APIAsyncTask<Result> {
    private var doBeforeMethod: SimpleMethod? = null
    private var doAfterMethod: SimpleMethod? = null
    private var onErrorMethod: OnErrorMethod? = null

    private var uiErrorView: UIErrorView? = null

    private lateinit var requestMethod: RequestMethod<Result>
    private var resultHandlerMethod: (HandleResultMethod<Result>)? = null

    private var apiTask: APITask<Result>? = null

    fun doBefore(doBeforeMethod: SimpleMethod): APIAsyncTask<Result> {
        this.doBeforeMethod = doBeforeMethod
        return this
    }

    fun request(requestMethod: RequestMethod<Result>): APIAsyncTask<Result> {
        this.requestMethod = requestMethod
        return this
    }

    fun handleResult(resultHandlerMethod: HandleResultMethod<Result>): APIAsyncTask<Result> {
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
        if (!hasFinished()) {
            throw RuntimeException("Cannot run new task, because the old one has not finish yet!")
        }

        doBeforeMethod?.invoke()

        apiTask = APITask(requestMethod,
            resultHandlerMethod,
            onErrorMethod,
            doAfterMethod,
            uiErrorView)

        apiTask!!.execute()
    }

    fun stopIfRunning() {
        if (apiTask != null && !hasFinished()) {
            apiTask!!.cancel(true)
        }
    }

    private fun hasFinished(): Boolean =
        apiTask != null && apiTask!!.status == AsyncTask.Status.FINISHED

    private class APITask<Result>(private val requestMethod: RequestMethod<Result>,
                                  private val resultHandlerMethod: HandleResultMethod<Result>?,
                                  private val onErrorMethod: OnErrorMethod?,
                                  private val doAfterMethod: SimpleMethod?,
                                  private val uiErrorView: UIErrorView?)
        : AsyncTask<Void, Void, Result>() {

        private var catchedException: Exception? = null

        override fun doInBackground(vararg args: Void?): Result? {
            try {
                return requestMethod.invoke()
            } catch (ex: Exception) {
                ex.printStackTrace()
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