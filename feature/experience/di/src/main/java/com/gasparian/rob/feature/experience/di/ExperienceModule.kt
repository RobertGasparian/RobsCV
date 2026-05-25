package com.gasparian.rob.feature.experience.di

import com.gasparian.rob.feature.experience.data.remote.ExperienceRemoteDataSource
import com.gasparian.rob.feature.experience.data.repository.NetworkBackedRcvExperienceRepository
import com.gasparian.rob.feature.experience.domain.repository.ExperienceRepository
import com.gasparian.rob.feature.experience.domain.usecase.ClearExperienceCacheUseCase
import com.gasparian.rob.feature.experience.domain.usecase.GetExperienceUseCase
import com.gasparian.rob.feature.experience.domain.usecase.SyncExperienceUseCase
import com.gasparian.rob.feature.experience.presentation.ExperienceViewModel
import org.koin.core.module.dsl.viewModel
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
        single {
            ClearExperienceCacheUseCase(
                experienceRepository = get(),
            )
        }
        single {
            SyncExperienceUseCase(
                experienceRepository = get(),
            )
        }
        viewModel {
            ExperienceViewModel(
                getExperienceUseCase = get(),
                clearExperienceCacheUseCase = get(),
                syncExperienceUseCase = get(),
            )
        }
    }
