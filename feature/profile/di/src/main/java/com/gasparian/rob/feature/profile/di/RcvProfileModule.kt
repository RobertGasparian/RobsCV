package com.gasparian.rob.feature.profile.di

import com.gasparian.rob.feature.profile.data.remote.RcvProfileRemoteDataSource
import com.gasparian.rob.feature.profile.data.repository.NetworkBackedRcvProfileRepository
import com.gasparian.rob.feature.profile.domain.repository.RcvProfileRepository
import org.koin.dsl.module

val rcvProfileModule =
    module {
        single {
            RcvProfileRemoteDataSource(
                networkClient = get(),
            )
        }
        single<RcvProfileRepository> {
            NetworkBackedRcvProfileRepository(
                remoteDataSource = get(),
                profileDao = get(),
            )
        }
    }
