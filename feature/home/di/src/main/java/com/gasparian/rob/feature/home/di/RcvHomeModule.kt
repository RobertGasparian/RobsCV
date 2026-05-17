package com.gasparian.rob.feature.home.di

import com.gasparian.rob.feature.home.data.remote.RcvHomeRemoteDataSource
import com.gasparian.rob.feature.home.data.repository.NetworkOnlyRcvHomeRepository
import com.gasparian.rob.feature.home.domain.repository.RcvHomeRepository
import org.koin.dsl.module

val rcvHomeModule =
    module {
        single {
            RcvHomeRemoteDataSource(
                networkClient = get(),
            )
        }
        single<RcvHomeRepository> {
            NetworkOnlyRcvHomeRepository(
                remoteDataSource = get(),
            )
        }
    }
