package com.gasparian.rob.feature.education.di

import com.gasparian.rob.feature.education.data.remote.RcvEducationRemoteDataSource
import com.gasparian.rob.feature.education.data.repository.NetworkBackedRcvEducationRepository
import com.gasparian.rob.feature.education.domain.repository.RcvEducationRepository
import org.koin.dsl.module

val rcvEducationModule =
    module {
        single {
            RcvEducationRemoteDataSource(
                networkClient = get(),
            )
        }
        single<RcvEducationRepository> {
            NetworkBackedRcvEducationRepository(
                remoteDataSource = get(),
                educationDao = get(),
            )
        }
    }
