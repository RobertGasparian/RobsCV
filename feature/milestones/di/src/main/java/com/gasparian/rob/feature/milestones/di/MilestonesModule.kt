package com.gasparian.rob.feature.milestones.di

import com.gasparian.rob.feature.milestones.data.remote.MilestonesRemoteDataSource
import com.gasparian.rob.feature.milestones.data.repository.NetworkBackedRcvMilestonesRepository
import com.gasparian.rob.feature.milestones.domain.repository.MilestonesRepository
import com.gasparian.rob.feature.milestones.domain.usecase.GetMilestonesUseCase
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
    }
