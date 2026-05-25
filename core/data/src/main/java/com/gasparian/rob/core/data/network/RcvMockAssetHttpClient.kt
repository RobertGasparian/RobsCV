package com.gasparian.rob.core.data.network

import android.content.Context
import com.gasparian.rob.core.network.RcvNetworkConfig
import com.gasparian.rob.core.network.createRcvJson
import com.gasparian.rob.core.network.installRcvHttpDefaults
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.serialization.json.Json

private const val RCV_MOCK_API_ASSET_ROOT = "mock-api"
private const val RCV_MOCK_API_VERSION = "v1"

fun createRcvMockAssetHttpClient(
    context: Context,
    config: RcvNetworkConfig,
    json: Json = createRcvJson(),
): HttpClient {
    val appContext = context.applicationContext

    return HttpClient(
        MockEngine { request ->
            val assetPath = request.url.encodedPath.toMockAssetPath()
            val content = appContext.assets.open(assetPath).bufferedReader().use { reader -> reader.readText() }

            respond(
                content = content,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        },
    ) {
        installRcvHttpDefaults(
            config = config,
            json = json,
        )
    }
}

private fun String.toMockAssetPath(): String {
    val endpointPath = trim('/')
        .removePrefix("$RCV_MOCK_API_VERSION/")
        .removeSuffix(".json")

    return "$RCV_MOCK_API_ASSET_ROOT/$RCV_MOCK_API_VERSION/$endpointPath.json"
}
