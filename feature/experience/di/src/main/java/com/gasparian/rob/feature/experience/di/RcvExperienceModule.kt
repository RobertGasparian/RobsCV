package com.gasparian.rob.feature.experience.di

import com.gasparian.rob.feature.experience.data.remote.RcvExperienceRemoteDataSource
import com.gasparian.rob.feature.experience.data.repository.NetworkBackedRcvExperienceRepository
import com.gasparian.rob.feature.experience.domain.repository.RcvExperienceRepository
import org.koin.dsl.module

val rcvExperienceModule =
    module {
        single {
            RcvExperienceRemoteDataSource(
                networkClient = get(),
            )
        }
        single<RcvExperienceRepository> {
            NetworkBackedRcvExperienceRepository(
                remoteDataSource = get(),
                experienceDao = get(),
            )
        }
    }
