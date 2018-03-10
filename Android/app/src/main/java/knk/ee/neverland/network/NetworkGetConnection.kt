package knk.ee.neverland.network

import knk.ee.neverland.utils.Constants
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

class NetworkGetConnection(private val link: String) {
    private var params: Int = 0
    private var action: String? = null
    private val paramsBuilder: StringBuilder = StringBuilder()
    private var onFailed: ((code: Int) -> Unit)? = null
    private var alreadyStarted: Boolean = false

    fun setAction(action: String): NetworkGetConnection {
        throwExceptionIfAlreadyStarted()

        this.action = action
        return this
    }

    fun addParam(key: String, value: String): NetworkGetConnection {
        throwExceptionIfAlreadyStarted()

        if (params++ > 0) {
            paramsBuilder.append("&")
        }

        paramsBuilder
                .append(key)
                .append("=")
                .append(value)

        return this
    }

    fun onFailed(action: (code: Int) -> Unit): NetworkGetConnection {
        throwExceptionIfAlreadyStarted()

        this.onFailed = action
        return this
    }

    fun getContent(): String? {
        try {
            throwExceptionIfAlreadyStarted()

            if (action == null) {
                throw IllegalArgumentException("Action is not set!")
            }

            val connection = makeConnection()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                onFailed?.invoke(connection.responseCode)
                return null
            }

            val data = connection.inputStream.bufferedReader().readText()
            connection.disconnect()
            return data
        } catch (ex: UnknownHostException) {
            onFailed?.invoke(Constants.NETWORK_ERROR)
            return null
        }
    }

    private fun makeURL(): URL {
        val query = "$link/$action?$paramsBuilder"
        println("GET Query: $query")
        return URL(query)
    }

    private fun makeConnection(): HttpURLConnection {
        return makeURL().openConnection() as HttpURLConnection
    }

    private fun throwExceptionIfAlreadyStarted() {
        if (alreadyStarted) {
            throw RuntimeException("Cannot run this method, because connection is already opened!")
        }
    }
}
