package com.gasparian.rob.feature.home.domain.model

import com.gasparian.rob.feature.education.domain.model.RcvEducation
import com.gasparian.rob.feature.experience.domain.model.RcvExperience
import com.gasparian.rob.feature.milestones.domain.model.RcvMilestones
import com.gasparian.rob.feature.profile.domain.model.RcvProfile
import com.gasparian.rob.feature.skills.domain.model.RcvSkills

data class RcvHomeData(
    val profile: RcvProfile,
    val skills: RcvSkills,
    val experience: RcvExperience,
    val education: RcvEducation,
    val milestones: RcvMilestones,
)
