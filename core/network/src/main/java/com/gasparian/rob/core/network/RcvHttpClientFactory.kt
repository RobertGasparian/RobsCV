package com.gasparian.rob.core.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createRcvJson(): Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

fun createRcvHttpClient(
    engineFactory: HttpClientEngineFactory<*>,
    config: RcvNetworkConfig,
    json: Json = createRcvJson(),
): HttpClient = HttpClient(engineFactory) {
    installRcvHttpDefaults(config, json)
}

fun HttpClientConfig<*>.installRcvHttpDefaults(
    config: RcvNetworkConfig,
    json: Json,
) {
    expectSuccess = false

    install(ContentNegotiation) {
        json(json)
    }

    install(HttpTimeout) {
        requestTimeoutMillis = config.requestTimeoutMillis
        connectTimeoutMillis = config.connectTimeoutMillis
        socketTimeoutMillis = config.socketTimeoutMillis
    }

    defaultRequest {
        url(config.baseUrl)
        accept(ContentType.Application.Json)
    }
}
