package com.gasparian.rob.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Builder
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.gasparian.rob.feature.education.data.local.RcvEducationDao
import com.gasparian.rob.feature.education.data.local.RcvEducationItemEntity
import com.gasparian.rob.feature.education.data.local.RcvEducationLocationEntity
import com.gasparian.rob.feature.education.data.local.RcvInstitutionEntity
import com.gasparian.rob.feature.experience.data.local.RcvExperienceDao
import com.gasparian.rob.feature.experience.data.local.RcvExperienceHighlightEntity
import com.gasparian.rob.feature.experience.data.local.RcvExperienceRoleEntity
import com.gasparian.rob.feature.milestones.data.local.RcvCurrentFocusEntity
import com.gasparian.rob.feature.milestones.data.local.RcvCurrentFocusTopicEntity
import com.gasparian.rob.feature.milestones.data.local.RcvMilestoneEntity
import com.gasparian.rob.feature.milestones.data.local.RcvMilestoneTopicEntity
import com.gasparian.rob.feature.milestones.data.local.RcvMilestonesDao
import com.gasparian.rob.feature.profile.data.local.RcvProfileContactEntity
import com.gasparian.rob.feature.profile.data.local.RcvProfileDao
import com.gasparian.rob.feature.profile.data.local.RcvProfileEntity
import com.gasparian.rob.feature.profile.data.local.RcvProfileLocationEntity
import com.gasparian.rob.feature.profile.data.local.RcvProfileQualificationEntity
import com.gasparian.rob.feature.skills.data.local.RcvSkillCategoryEntity
import com.gasparian.rob.feature.skills.data.local.RcvSkillContextEntity
import com.gasparian.rob.feature.skills.data.local.RcvSkillEntity
import com.gasparian.rob.feature.skills.data.local.RcvSkillsDao
import kotlinx.coroutines.Dispatchers

const val RCV_DATABASE_NAME = "robscv.db"

@Database(
    entities = [
        RcvProfileEntity::class,
        RcvProfileLocationEntity::class,
        RcvProfileContactEntity::class,
        RcvProfileQualificationEntity::class,
        RcvSkillCategoryEntity::class,
        RcvSkillEntity::class,
        RcvSkillContextEntity::class,
        RcvExperienceRoleEntity::class,
        RcvExperienceHighlightEntity::class,
        RcvInstitutionEntity::class,
        RcvEducationLocationEntity::class,
        RcvEducationItemEntity::class,
        RcvCurrentFocusEntity::class,
        RcvCurrentFocusTopicEntity::class,
        RcvMilestoneEntity::class,
        RcvMilestoneTopicEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class RcvDatabase : RoomDatabase() {
    abstract fun profileDao(): RcvProfileDao

    abstract fun skillsDao(): RcvSkillsDao

    abstract fun experienceDao(): RcvExperienceDao

    abstract fun educationDao(): RcvEducationDao

    abstract fun milestonesDao(): RcvMilestonesDao
}

fun buildRcvDatabase(
    builder: Builder<RcvDatabase>,
): RcvDatabase = builder
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
