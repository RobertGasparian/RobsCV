package com.gasparian.rob.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

class KtorRcvNetworkClientTest {
    @Test
    fun `get maps successful JSON response`() = runTest {
        val networkClient =
            KtorRcvNetworkClient(
                httpClient =
                testHttpClient {
                    assertEquals("/v1/profile", it.url.encodedPath)
                    respondJson("""{"displayName":"Robert Gasparyan"}""")
                },
            )

        val result = networkClient.get("profile") { body<TestProfileDto>() }

        when (result) {
            is RcvNetworkResult.Success -> assertEquals("Robert Gasparyan", result.data.displayName)
            is RcvNetworkResult.Failure -> fail("Expected success but got ${result.error}")
        }
    }

    @Test
    fun `get maps non-success response to HTTP error`() = runTest {
        val networkClient =
            KtorRcvNetworkClient(
                httpClient =
                testHttpClient {
                    respondJson(
                        content = """{"error":"not found"}""",
                        status = HttpStatusCode.NotFound,
                    )
                },
            )

        val result = networkClient.get("skills") { body<TestProfileDto>() }

        when (result) {
            is RcvNetworkResult.Success -> fail("Expected failure but got ${result.data}")

            is RcvNetworkResult.Failure ->
                assertEquals(
                    RcvNetworkError.Http(
                        code = 404,
                        message = "Not Found",
                    ),
                    result.error,
                )
        }
    }

    private fun testHttpClient(handler: MockEngineHandler): HttpClient = HttpClient(MockEngine) {
        engine {
            addHandler(handler)
        }
        installRcvHttpDefaults(
            config =
            RcvNetworkConfig(
                baseUrl = "https://example.com/v1/",
            ),
            json = createRcvJson(),
        )
    }

    private fun MockRequestHandleScope.respondJson(
        content: String,
        status: HttpStatusCode = HttpStatusCode.OK,
    ) = respond(
        content = content,
        status = status,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
    )
}

@Serializable
private data class TestProfileDto(
    val displayName: String,
)

private typealias MockEngineHandler = suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
