package com.gasparian.rob.feature.home.domain.model

import com.gasparian.rob.feature.education.domain.model.Education
import com.gasparian.rob.feature.experience.domain.model.Experience
import com.gasparian.rob.feature.milestones.domain.model.Milestones
import com.gasparian.rob.feature.profile.domain.model.Profile
import com.gasparian.rob.feature.skills.domain.model.Skills

data class HomeData(
    val profile: Profile,
    val skills: Skills,
    val experience: Experience,
    val education: Education,
    val milestones: Milestones,
)
