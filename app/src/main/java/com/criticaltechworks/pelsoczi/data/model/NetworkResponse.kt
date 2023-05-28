package com.criticaltechworks.pelsoczi.data.model

import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response

/**
 * Convenience result classes for [Request] to wrap responses and platform network exceptions.
 */
sealed interface NetworkResponse {

    /**
     * A [Response] was received from the server.
     *
     * @param code the server code for the request.
     * @param body the json content body requested.
     */
    data class Ok(
        val code: Int,
        val body: String,
    ) : NetworkResponse {
        /**
         * Indicate if a specific [Request] was successfully completed and
         * successfully handled by the server.
         *
         * @return `true` when [code] is 200-299, `false` otherwise.
         */
        val isSuccess: Boolean
            get() = when (this.code) {
                in 100 .. 199 -> false // informational responses
                in 200 .. 299 -> true // success responses
                in 300 .. 399 -> false // redirection messages
                in 400 .. 499 -> false // client error responses
                in 500 .. 599 -> false // server error responses
                else -> false // unknown
            }
    }

    /**
     * An exception occurred on the network layer, usually no internet available or there was
     * a problem with the [HttpUrl] for the [Request].
     *
     * @param ex the exception encountered during the request attempt.
     */
    data class Failure(
        val ex: Exception
    ): NetworkResponse

}
