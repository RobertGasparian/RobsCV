package com.gasparian.rob

import android.app.Application
import com.gasparian.rob.di.appModule
import com.gasparian.rob.feature.contact.di.contactModule
import com.gasparian.rob.feature.education.di.educationModule
import com.gasparian.rob.feature.experience.di.experienceModule
import com.gasparian.rob.feature.home.di.homeModule
import com.gasparian.rob.feature.milestones.di.milestonesModule
import com.gasparian.rob.feature.profile.di.profileModule
import com.gasparian.rob.feature.skills.di.skillsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RcvApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RcvApplication)
            modules(
                appModule,
                homeModule,
                contactModule,
                educationModule,
                experienceModule,
                milestonesModule,
                profileModule,
                skillsModule,
            )
        }
    }
}
