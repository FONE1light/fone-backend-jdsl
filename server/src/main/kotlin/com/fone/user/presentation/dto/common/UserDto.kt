package com.fone.user.presentation.dto.common

import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.SocialLoginType
import java.time.LocalDate

data class UserDto(
    val id: Long,
    val job: Job,
    val interests: List<CategoryType>,
    val nickname: String,
    val birthday: LocalDate?,
    val gender: Gender,
    val profileUrl: String,
    val phoneNumber: String,
    val email: String,
    val socialLoginType: SocialLoginType,
    val agreeToTermsOfServiceTermsOfUse: Boolean,
    val agreeToPersonalInformation: Boolean,
    val isReceiveMarketing: Boolean,
    val enabled: Boolean,
) {

    constructor(
        user: User,
    ) : this(
        id = user.id!!,
        job = user.job,
        interests = user.interests.map { CategoryType(it) }.toList(),
        nickname = user.nickname,
        birthday = user.birthday,
        gender = user.gender,
        profileUrl = user.profileUrl,
        phoneNumber = user.phoneNumber,
        email = user.email,
        socialLoginType = user.socialLoginType,
        agreeToTermsOfServiceTermsOfUse = user.agreeToTermsOfServiceTermsOfUse,
        agreeToPersonalInformation = user.agreeToPersonalInformation,
        isReceiveMarketing = user.isReceiveMarketing,
        enabled = user.enabled
    )
}
