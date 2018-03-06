package knk.ee.neverland.network

import java.net.HttpURLConnection
import java.net.URL

class NetworkGetConnection(private val link: String) {
    private var params: Int = 0
    private var action: String = ""
    private val paramsBuilder: StringBuilder = StringBuilder()
    private var onFailed: (code: Int) -> Unit = {}

    fun setAction(action: String): NetworkGetConnection {
        this.action = action
        return this
    }

    fun addParam(key: String, value: String): NetworkGetConnection {
        if (params++ > 0) {
            paramsBuilder.append("&")
        }

        paramsBuilder
                .append(key)
                .append("=")
                .append(value)

        return this
    }

    private fun makeURL(): URL {
        val query = "$link/$action?$paramsBuilder"
        println("GET Query: $query")
        return URL(query)
    }

    private fun makeConnection(): HttpURLConnection {
        return makeURL().openConnection() as HttpURLConnection
    }

    fun onFailed(action: (code: Int) -> Unit): NetworkGetConnection {
        this.onFailed = action
        return this
    }

    fun getContent(): String {
        val connection = makeConnection()

        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
            onFailed(connection.responseCode)
        }

        val data = connection.inputStream.bufferedReader().readText()
        connection.disconnect()
        return data
    }
}

class NetworkPostConnection(private val link: String, private val action: String) {
    private var onFailed: (code: Int) -> Unit = {}
    private val connection: HttpURLConnection = URL("$link/$action").openConnection() as HttpURLConnection

    private var params: Int = 0
    private val paramsBuilder: StringBuilder = StringBuilder()

    init {
        connection.requestMethod = "POST"
        connection.addRequestProperty("User-agent", "Mozilla/5.0")
        connection.setRequestProperty("Accept-Language", "UTF-8")
        connection.setRequestProperty("Content-Type", "application/json; charset=utf8")
    }

    fun addParam(key: String, value: String): NetworkPostConnection {
        if (params++ > 0) {
            paramsBuilder.append("&")
        }

        paramsBuilder
                .append(key)
                .append("=")
                .append(value)


        return this
    }

    fun addText(test: String) : NetworkPostConnection {
        paramsBuilder.append(test)
        return this
    }

    fun onFailed(action: (code: Int) -> Unit): NetworkPostConnection {
        this.onFailed = action
        return this
    }

    private fun writeParams() {
        connection.doOutput = true
        val outputStream = connection.outputStream
        val data = paramsBuilder.toString()
        println(data)
        outputStream.write(data.toByteArray())
        outputStream.flush()
        outputStream.close()
    }

    fun getContent(): String {
        writeParams()

        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
            onFailed(connection.responseCode)
        }

        val data = connection.inputStream.bufferedReader().readText()
        connection.disconnect()
        return data
    }
}