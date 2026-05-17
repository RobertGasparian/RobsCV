package com.gasparian.rob.di

import com.gasparian.rob.core.network.KtorRcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkConfig
import com.gasparian.rob.core.network.RcvNetworkConstants
import com.gasparian.rob.core.network.createRcvHttpClient
import io.ktor.client.engine.android.Android
import org.koin.dsl.module

val rcvAppModule =
    module {
        single {
            RcvNetworkConfig(
                baseUrl = RcvNetworkConstants.DEFAULT_BASE_URL,
            )
        }
        single {
            createRcvHttpClient(
                engineFactory = Android,
                config = get(),
            )
        }
        single<RcvNetworkClient> {
            KtorRcvNetworkClient(
                httpClient = get(),
            )
        }
    }
