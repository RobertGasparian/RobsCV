package com.gasparian.rob.feature.profile.di

import com.gasparian.rob.feature.profile.data.remote.ProfileRemoteDataSource
import com.gasparian.rob.feature.profile.data.repository.NetworkBackedRcvProfileRepository
import com.gasparian.rob.feature.profile.domain.repository.ProfileRepository
import com.gasparian.rob.feature.profile.domain.usecase.ClearProfileCacheUseCase
import com.gasparian.rob.feature.profile.domain.usecase.GetProfileUseCase
import com.gasparian.rob.feature.profile.domain.usecase.SyncProfileUseCase
import com.gasparian.rob.feature.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule =
    module {
        single {
            ProfileRemoteDataSource(
                networkClient = get(),
            )
        }
        single<ProfileRepository> {
            NetworkBackedRcvProfileRepository(
                remoteDataSource = get(),
                profileDao = get(),
            )
        }
        single {
            GetProfileUseCase(
                profileRepository = get(),
            )
        }
        single {
            ClearProfileCacheUseCase(
                profileRepository = get(),
            )
        }
        single {
            SyncProfileUseCase(
                profileRepository = get(),
            )
        }
        viewModel {
            ProfileViewModel(
                getProfileUseCase = get(),
                clearProfileCacheUseCase = get(),
                syncProfileUseCase = get(),
            )
        }
    }
