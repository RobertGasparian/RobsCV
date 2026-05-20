package com.gasparian.rob.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Builder
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.gasparian.rob.feature.education.data.local.EducationDao
import com.gasparian.rob.feature.education.data.local.EducationItemEntity
import com.gasparian.rob.feature.education.data.local.EducationLocationEntity
import com.gasparian.rob.feature.education.data.local.InstitutionEntity
import com.gasparian.rob.feature.experience.data.local.ExperienceDao
import com.gasparian.rob.feature.experience.data.local.ExperienceHighlightEntity
import com.gasparian.rob.feature.experience.data.local.ExperienceRoleEntity
import com.gasparian.rob.feature.milestones.data.local.CurrentFocusEntity
import com.gasparian.rob.feature.milestones.data.local.CurrentFocusTopicEntity
import com.gasparian.rob.feature.milestones.data.local.MilestoneEntity
import com.gasparian.rob.feature.milestones.data.local.MilestoneTopicEntity
import com.gasparian.rob.feature.milestones.data.local.MilestonesDao
import com.gasparian.rob.feature.profile.data.local.ProfileContactEntity
import com.gasparian.rob.feature.profile.data.local.ProfileDao
import com.gasparian.rob.feature.profile.data.local.ProfileEntity
import com.gasparian.rob.feature.profile.data.local.ProfileLocationEntity
import com.gasparian.rob.feature.profile.data.local.ProfileQualificationEntity
import com.gasparian.rob.feature.skills.data.local.SkillCategoryEntity
import com.gasparian.rob.feature.skills.data.local.SkillContextEntity
import com.gasparian.rob.feature.skills.data.local.SkillEntity
import com.gasparian.rob.feature.skills.data.local.SkillsDao
import kotlinx.coroutines.Dispatchers

const val RCV_DATABASE_NAME = "robscv.db"

@Database(
    entities = [
        ProfileEntity::class,
        ProfileLocationEntity::class,
        ProfileContactEntity::class,
        ProfileQualificationEntity::class,
        SkillCategoryEntity::class,
        SkillEntity::class,
        SkillContextEntity::class,
        ExperienceRoleEntity::class,
        ExperienceHighlightEntity::class,
        InstitutionEntity::class,
        EducationLocationEntity::class,
        EducationItemEntity::class,
        CurrentFocusEntity::class,
        CurrentFocusTopicEntity::class,
        MilestoneEntity::class,
        MilestoneTopicEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class RcvDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    abstract fun skillsDao(): SkillsDao

    abstract fun experienceDao(): ExperienceDao

    abstract fun educationDao(): EducationDao

    abstract fun milestonesDao(): MilestonesDao
}

fun buildRcvDatabase(
    builder: Builder<RcvDatabase>,
): RcvDatabase = builder
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
