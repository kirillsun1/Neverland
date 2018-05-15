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
import java.net.UnknownHostException

object NetworkRequester {
    fun makeGetRequestAndGetResponseBody(urlString: String): String {
        try {
            val request = Request.Builder()
                .url(URL(urlString))
                .get()
                .build()

            val client = OkHttpClient.Builder()
                .build()

            val response = client.newCall(request).execute()

            throwNetworkExceptionIfResponseIsNotSuccessful(response)

            return response.body()!!.string()
        } catch (ex: SocketTimeoutException) {
            throw NetworkException(Constants.NETWORK_TIMEOUT)
        } catch (ex: UnknownHostException) {
            throw NetworkException(Constants.NETWORK_ERROR_CODE)
        }
    }

    fun makePostRequestAndGetResponseBody(
        urlString: String, requestBody: RequestBody): String {
        try {
            val request = Request.Builder()
                .url(urlString)
                .post(requestBody)
                .build()

            val client = OkHttpClient.Builder()
                .build()

            val response = client.newCall(request).execute()

            throwNetworkExceptionIfResponseIsNotSuccessful(response)

            return response.body()!!.string()
        } catch (ex: SocketTimeoutException) {
            throw NetworkException(Constants.NETWORK_TIMEOUT)
        } catch (ex: UnknownHostException) {
            throw NetworkException(Constants.NETWORK_ERROR_CODE)
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
}