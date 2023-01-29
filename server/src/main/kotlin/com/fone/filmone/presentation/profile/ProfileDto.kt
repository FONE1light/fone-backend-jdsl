package com.fone.filmone.presentation.profile

import com.fone.common.utils.DateTimeFormat
import com.fone.filmone.domain.common.Career
import com.fone.filmone.domain.common.Domain
import com.fone.filmone.domain.common.Gender
import com.fone.filmone.domain.common.Interest
import com.fone.filmone.domain.profile.entity.Profile
import com.fone.filmone.domain.profile.entity.ProfileWant
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
    val interests: List<Interest>,
    val domains: List<Domain>,
    val profileImages: List<String>,
    val viewCount: Long,

    val isWant: Boolean = false,
    val age: Int,
) {
    constructor(profile: Profile, userProfileWantMap: Map<Long, ProfileWant?>) : this(
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
        interests = profile.interests.map { Interest(it) }.toList(),
        domains = profile.domains.map { Domain(it) }.toList(),
        profileImages = listOf(),
        viewCount = profile.viewCount,
        isWant = userProfileWantMap.get(profile.id!!) != null,
        age = DateTimeFormat.calculateAge(profile.birthday)
    )
}