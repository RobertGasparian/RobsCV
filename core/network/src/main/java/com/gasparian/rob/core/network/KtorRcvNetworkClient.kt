package com.gasparian.rob.core.network

import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import io.ktor.serialization.JsonConvertException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

class KtorRcvNetworkClient(
    private val httpClient: HttpClient,
) : RcvNetworkClient {
    override suspend fun <T> get(
        path: String,
        responseMapper: suspend RcvNetworkResponse.() -> T,
    ): RcvNetworkResult<T> = safeNetworkCall {
        val response = httpClient.get(path)
        if (response.status.isSuccess()) {
            RcvNetworkResult.Success(responseMapper(RcvNetworkResponse(response)))
        } else {
            RcvNetworkResult.Failure(
                RcvNetworkError.Http(
                    code = response.status.value,
                    message = response.status.description,
                ),
            )
        }
    }
}

internal inline fun <T> safeNetworkCall(block: () -> RcvNetworkResult<T>): RcvNetworkResult<T> = try {
    block()
} catch (_: HttpRequestTimeoutException) {
    RcvNetworkResult.Failure(RcvNetworkError.Timeout)
} catch (_: ConnectTimeoutException) {
    RcvNetworkResult.Failure(RcvNetworkError.Timeout)
} catch (_: SocketTimeoutException) {
    RcvNetworkResult.Failure(RcvNetworkError.Timeout)
} catch (_: JsonConvertException) {
    RcvNetworkResult.Failure(RcvNetworkError.Serialization)
} catch (_: SerializationException) {
    RcvNetworkResult.Failure(RcvNetworkError.Serialization)
} catch (_: IOException) {
    RcvNetworkResult.Failure(RcvNetworkError.NetworkUnavailable)
} catch (exception: CancellationException) {
    throw exception
} catch (exception: Throwable) {
    RcvNetworkResult.Failure(RcvNetworkError.Unknown(exception.message))
}
