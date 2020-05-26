package com.vvechirko.weatherapp.ui.base

import com.vvechirko.weatherapp.util.NetworkManager
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ErrorParser(
    private val networkManager: NetworkManager
) {
    fun parse(t: Throwable): AppException {
        return when {
            t is AppException -> t
            !networkManager.isNetworkAvailable() -> AppException.General("no_internet_connection")
            t is SocketTimeoutException -> AppException.General("server_error")
            t is HttpException -> if (t.code() >= 500) AppException.General("server_error") else try {
                val s = t.response()!!.errorBody()!!.string()
                with(JSONObject(s)) {
                    AppException.Api(
                        getString("message"),
                        getInt("cod")
                    )
                }
            } catch (ignored: Throwable) {
                AppException.General(t.message())
            }
            else -> AppException.General(t.message ?: t.toString())
        }
    }
}

sealed class AppException(val title: String) : Exception(title) {
    class General(title: String) : AppException(title)
    class Api(title: String, val code: Int) : AppException(title)

    override fun toString(): String {
        return title
    }
}