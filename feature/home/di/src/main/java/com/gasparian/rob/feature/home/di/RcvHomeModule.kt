package com.gasparian.rob.feature.home.di

import com.gasparian.rob.feature.home.data.repository.CompositeRcvHomeRepository
import com.gasparian.rob.feature.home.domain.repository.RcvHomeRepository
import org.koin.dsl.module

val rcvHomeModule =
    module {
        single<RcvHomeRepository> {
            CompositeRcvHomeRepository(
                profileRepository = get(),
                skillsRepository = get(),
                experienceRepository = get(),
                educationRepository = get(),
                milestonesRepository = get(),
            )
        }
    }
