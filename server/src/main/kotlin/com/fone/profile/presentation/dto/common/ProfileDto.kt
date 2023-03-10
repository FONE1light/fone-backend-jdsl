package com.fone.profile.presentation.dto.common

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.utils.DateTimeFormat
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileWant
import java.time.LocalDate

data class ProfileDto(
    val id: Long,
    val name: String,
    val hookingComment: String,
    val birthday: LocalDate?,
    val gender: Gender,
    val height: Int,
    val weight: Int,
    val email: String,
    val sns: String,
    val specialty: String,
    val details: String,
    val career: Career,
    val categories: List<CategoryType>,
    val domains: List<DomainType>,
    val profileUrls: List<String>,
    val viewCount: Long,
    val profileUrl: String,
    val isWant: Boolean = false,
    val age: Int,
) {
    constructor(
        profile: Profile,
        userProfileWantMap: Map<Long, ProfileWant?>,
        profileUrls: List<String>,
        domains: List<DomainType>,
        categories: List<CategoryType>,
    ) : this(
        id = profile.id!!,
        name = profile.name,
        hookingComment = profile.hookingComment,
        birthday = profile.birthday,
        gender = profile.gender,
        height = profile.height,
        weight = profile.weight,
        email = profile.email,
        sns = profile.sns,
        specialty = profile.specialty,
        details = profile.details,
        career = profile.career,
        categories = categories,
        domains = domains,
        profileUrls = profileUrls,
        viewCount = profile.viewCount,
        isWant = userProfileWantMap.get(profile.id!!) != null,
        age = DateTimeFormat.calculateAge(profile.birthday),
        profileUrl = if (profileUrls.isEmpty()) "" else profileUrls[0]
    )
}
