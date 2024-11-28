package com.example.myapplication.ui.common

import android.content.Context
import com.example.myapplication.R
import erika.core.net.HttpStatusException
import java.net.SocketException
import java.net.UnknownHostException

/**
 * Get a user-friendly message from this exception.
 */
fun Exception.toMessage(context: Context): String {
    return when (this) {
        is HttpStatusException -> context.getString(R.string.message_server_error, statusCode)
        is SocketException, is UnknownHostException -> context.getString(R.string.message_no_connection)
        else -> message ?: toString()
    }
}