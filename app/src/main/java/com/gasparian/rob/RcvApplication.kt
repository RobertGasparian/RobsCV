package com.gasparian.rob

import android.app.Application
import com.gasparian.rob.di.rcvAppModule
import com.gasparian.rob.feature.contact.di.rcvContactModule
import com.gasparian.rob.feature.education.di.rcvEducationModule
import com.gasparian.rob.feature.experience.di.rcvExperienceModule
import com.gasparian.rob.feature.home.di.rcvHomeModule
import com.gasparian.rob.feature.milestones.di.rcvMilestonesModule
import com.gasparian.rob.feature.profile.di.rcvProfileModule
import com.gasparian.rob.feature.skills.di.rcvSkillsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RcvApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RcvApplication)
            modules(
                rcvAppModule,
                rcvHomeModule,
                rcvContactModule,
                rcvEducationModule,
                rcvExperienceModule,
                rcvMilestonesModule,
                rcvProfileModule,
                rcvSkillsModule,
            )
        }
    }
}
