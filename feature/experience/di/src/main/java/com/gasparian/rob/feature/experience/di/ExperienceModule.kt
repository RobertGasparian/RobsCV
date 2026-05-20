package com.gasparian.rob.feature.experience.di

import com.gasparian.rob.feature.experience.data.remote.ExperienceRemoteDataSource
import com.gasparian.rob.feature.experience.data.repository.NetworkBackedRcvExperienceRepository
import com.gasparian.rob.feature.experience.domain.repository.ExperienceRepository
import com.gasparian.rob.feature.experience.domain.usecase.GetExperienceUseCase
import org.koin.dsl.module

val experienceModule =
    module {
        single {
            ExperienceRemoteDataSource(
                networkClient = get(),
            )
        }
        single<ExperienceRepository> {
            NetworkBackedRcvExperienceRepository(
                remoteDataSource = get(),
                experienceDao = get(),
            )
        }
        single {
            GetExperienceUseCase(
                experienceRepository = get(),
            )
        }
    }
