package com.gasparian.rob.feature.skills.di

import com.gasparian.rob.feature.skills.data.remote.RcvSkillsRemoteDataSource
import com.gasparian.rob.feature.skills.data.repository.NetworkBackedRcvSkillsRepository
import com.gasparian.rob.feature.skills.domain.repository.RcvSkillsRepository
import org.koin.dsl.module

val rcvSkillsModule =
    module {
        single {
            RcvSkillsRemoteDataSource(
                networkClient = get(),
            )
        }
        single<RcvSkillsRepository> {
            NetworkBackedRcvSkillsRepository(
                remoteDataSource = get(),
                skillsDao = get(),
            )
        }
    }
