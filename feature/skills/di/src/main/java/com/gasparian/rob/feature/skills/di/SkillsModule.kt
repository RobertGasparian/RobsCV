package com.gasparian.rob.feature.skills.di

import com.gasparian.rob.feature.skills.data.remote.SkillsRemoteDataSource
import com.gasparian.rob.feature.skills.data.repository.NetworkBackedRcvSkillsRepository
import com.gasparian.rob.feature.skills.domain.repository.SkillsRepository
import com.gasparian.rob.feature.skills.domain.usecase.GetSkillsUseCase
import org.koin.dsl.module

val skillsModule =
    module {
        single {
            SkillsRemoteDataSource(
                networkClient = get(),
            )
        }
        single<SkillsRepository> {
            NetworkBackedRcvSkillsRepository(
                remoteDataSource = get(),
                skillsDao = get(),
            )
        }
        single {
            GetSkillsUseCase(
                skillsRepository = get(),
            )
        }
    }
