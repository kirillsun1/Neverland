package knk.ee.neverland.network

import knk.ee.neverland.exceptions.NetworkException
import knk.ee.neverland.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

object NetworkRequester {
    private val MAXIMUM_TIMEOUT_IN_MS = 10000
    private val TIMEOUT_DELTA = 2500
    private var currentTimeout = 5000

    fun makeGetRequestAndGetResponseBody(urlString: String,
                                         defaultTimeoutInMilliSeconds: Int = currentTimeout): String {
        try {
            val request = Request.Builder()
                .url(URL(urlString))
                .get()
                .build()

            val client = OkHttpClient.Builder()
                .connectTimeout(defaultTimeoutInMilliSeconds.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(defaultTimeoutInMilliSeconds.toLong(), TimeUnit.MILLISECONDS)
                .build()

            val response = client.newCall(request).execute()

            throwNetworkExceptionIfResponseIsNotSuccessful(response)

            return response.body()!!.string()
        } catch (ex: SocketTimeoutException) {
            increaseTimeout()
            throw NetworkException(Constants.NETWORK_TIMEOUT)
        }
    }

    fun makePostRequestAndGetResponseBody(urlString: String, requestBody: RequestBody,
                                          defaultTimeoutInMilliSeconds: Int = currentTimeout): String {
        try {
            val request = Request.Builder()
                .url(urlString)
                .post(requestBody)
                .build()

            val client = OkHttpClient.Builder()
                .connectTimeout(defaultTimeoutInMilliSeconds.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(defaultTimeoutInMilliSeconds.toLong(), TimeUnit.MILLISECONDS)
                .build()

            val response = client.newCall(request).execute()

            throwNetworkExceptionIfResponseIsNotSuccessful(response)

            return response.body()!!.string()
        } catch (ex: SocketTimeoutException) {
            increaseTimeout()
            throw NetworkException(Constants.NETWORK_TIMEOUT)
        }
    }

    private fun throwNetworkExceptionIfResponseIsNotSuccessful(response: Response) {
        if (!response.isSuccessful) {
            when (response.code()) {
                HttpURLConnection.HTTP_NOT_FOUND, HttpURLConnection.HTTP_INTERNAL_ERROR ->
                    throw NetworkException(Constants.BAD_REQUEST_TO_API_CODE)

                else -> throw NetworkException(Constants.NETWORK_ERROR_CODE)
            }
        }
    }

    private fun increaseTimeout() {
        currentTimeout = min(currentTimeout + TIMEOUT_DELTA, MAXIMUM_TIMEOUT_IN_MS)
    }
}