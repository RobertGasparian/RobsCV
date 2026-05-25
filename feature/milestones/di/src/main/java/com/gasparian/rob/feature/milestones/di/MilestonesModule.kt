package com.gasparian.rob.feature.milestones.di

import com.gasparian.rob.feature.milestones.data.remote.MilestonesRemoteDataSource
import com.gasparian.rob.feature.milestones.data.repository.NetworkBackedRcvMilestonesRepository
import com.gasparian.rob.feature.milestones.domain.repository.MilestonesRepository
import com.gasparian.rob.feature.milestones.domain.usecase.ClearMilestonesCacheUseCase
import com.gasparian.rob.feature.milestones.domain.usecase.GetMilestonesUseCase
import com.gasparian.rob.feature.milestones.domain.usecase.SyncMilestonesUseCase
import com.gasparian.rob.feature.milestones.presentation.MilestonesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val milestonesModule =
    module {
        single {
            MilestonesRemoteDataSource(
                networkClient = get(),
            )
        }
        single<MilestonesRepository> {
            NetworkBackedRcvMilestonesRepository(
                remoteDataSource = get(),
                milestonesDao = get(),
            )
        }
        single {
            GetMilestonesUseCase(
                milestonesRepository = get(),
            )
        }
        single {
            ClearMilestonesCacheUseCase(
                milestonesRepository = get(),
            )
        }
        single {
            SyncMilestonesUseCase(
                milestonesRepository = get(),
            )
        }
        viewModel {
            MilestonesViewModel(
                getMilestonesUseCase = get(),
                clearMilestonesCacheUseCase = get(),
                syncMilestonesUseCase = get(),
            )
        }
    }
