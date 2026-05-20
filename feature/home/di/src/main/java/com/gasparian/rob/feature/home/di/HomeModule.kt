package com.gasparian.rob.feature.home.di

import com.gasparian.rob.feature.home.data.repository.CompositeRcvHomeRepository
import com.gasparian.rob.feature.home.domain.repository.HomeRepository
import com.gasparian.rob.feature.home.domain.usecase.GetHomeDataUseCase
import com.gasparian.rob.feature.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule =
    module {
        single<HomeRepository> {
            CompositeRcvHomeRepository(
                profileRepository = get(),
                skillsRepository = get(),
                experienceRepository = get(),
                educationRepository = get(),
                milestonesRepository = get(),
            )
        }
        single {
            GetHomeDataUseCase(
                homeRepository = get(),
            )
        }
        viewModel {
            HomeViewModel(
                getHomeDataUseCase = get(),
            )
        }
    }
