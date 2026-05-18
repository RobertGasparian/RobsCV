package com.gasparian.rob.di

import androidx.room.Room
import com.gasparian.rob.core.network.KtorRcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkConfig
import com.gasparian.rob.core.network.RcvNetworkConstants
import com.gasparian.rob.core.network.createRcvHttpClient
import com.gasparian.rob.core.network.createRcvJson
import com.gasparian.rob.database.RCV_DATABASE_NAME
import com.gasparian.rob.database.RcvDatabase
import com.gasparian.rob.database.buildRcvDatabase
import io.ktor.client.engine.android.Android
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val rcvAppModule =
    module {
        single {
            createRcvJson()
        }
        single {
            RcvNetworkConfig(
                baseUrl = RcvNetworkConstants.DEFAULT_BASE_URL,
            )
        }
        single {
            createRcvHttpClient(
                engineFactory = Android,
                config = get(),
                json = get(),
            )
        }
        single<RcvNetworkClient> {
            KtorRcvNetworkClient(
                httpClient = get(),
            )
        }
        single {
            val appContext = androidContext().applicationContext
            val databaseFile = appContext.getDatabasePath(RCV_DATABASE_NAME)

            Room.databaseBuilder<RcvDatabase>(
                context = appContext,
                name = databaseFile.absolutePath,
            )
        }
        single {
            buildRcvDatabase(
                builder = get(),
            )
        }
        single {
            get<RcvDatabase>().profileDao()
        }
        single {
            get<RcvDatabase>().skillsDao()
        }
        single {
            get<RcvDatabase>().experienceDao()
        }
        single {
            get<RcvDatabase>().educationDao()
        }
        single {
            get<RcvDatabase>().milestonesDao()
        }
    }
