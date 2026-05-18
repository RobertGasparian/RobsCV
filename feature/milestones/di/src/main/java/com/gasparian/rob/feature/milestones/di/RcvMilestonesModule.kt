package com.gasparian.rob.feature.milestones.di

import com.gasparian.rob.feature.milestones.data.remote.RcvMilestonesRemoteDataSource
import com.gasparian.rob.feature.milestones.data.repository.NetworkBackedRcvMilestonesRepository
import com.gasparian.rob.feature.milestones.domain.repository.RcvMilestonesRepository
import org.koin.dsl.module

val rcvMilestonesModule =
    module {
        single {
            RcvMilestonesRemoteDataSource(
                networkClient = get(),
            )
        }
        single<RcvMilestonesRepository> {
            NetworkBackedRcvMilestonesRepository(
                remoteDataSource = get(),
                milestonesDao = get(),
            )
        }
    }
