package com.gasparian.rob.feature.education.di

import com.gasparian.rob.feature.education.data.remote.EducationRemoteDataSource
import com.gasparian.rob.feature.education.data.repository.NetworkBackedRcvEducationRepository
import com.gasparian.rob.feature.education.domain.repository.EducationRepository
import com.gasparian.rob.feature.education.domain.usecase.ClearEducationCacheUseCase
import com.gasparian.rob.feature.education.domain.usecase.GetEducationUseCase
import com.gasparian.rob.feature.education.domain.usecase.SyncEducationUseCase
import com.gasparian.rob.feature.education.presentation.EducationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val educationModule =
    module {
        single {
            EducationRemoteDataSource(
                networkClient = get(),
            )
        }
        single<EducationRepository> {
            NetworkBackedRcvEducationRepository(
                remoteDataSource = get(),
                educationDao = get(),
            )
        }
        single {
            GetEducationUseCase(
                educationRepository = get(),
            )
        }
        single {
            ClearEducationCacheUseCase(
                educationRepository = get(),
            )
        }
        single {
            SyncEducationUseCase(
                educationRepository = get(),
            )
        }
        viewModel {
            EducationViewModel(
                getEducationUseCase = get(),
                clearEducationCacheUseCase = get(),
                syncEducationUseCase = get(),
            )
        }
    }
