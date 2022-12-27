package com.fone.filmone.presentation.auth

import com.fone.filmone.domain.user.entity.User
import com.fone.filmone.domain.user.enum.Gender
import com.fone.filmone.domain.user.enum.Interest
import com.fone.filmone.domain.user.enum.Job
import com.fone.filmone.domain.user.enum.SocialLoginType

class SignUpDto {

    data class SignUpRequest(
        val job: Job,
        val interests: List<Interest>,
        val nickname: String,
        val birthday: String,
        val gender: Gender,
        val profileUrl: String,
        val phoneNumber: String,
        val email: String,
        val socialLoginType: SocialLoginType,
        val agreeToTermsOfServiceTermsOfUse: Boolean,
        val agreeToPersonalInformation: Boolean,
        val isReceiveMarketing: Boolean,
    ) {
        fun toEntity(): User {

            return User(
                job = job,
                interests = interests.joinToString(","),
                nickname = nickname,
                birthday = birthday,
                gender = gender,
                profileUrl = profileUrl,
                phoneNumber = phoneNumber,
                email = email,
                socialLoginType = socialLoginType,
                agreeToTermsOfServiceTermsOfUse = agreeToTermsOfServiceTermsOfUse,
                agreeToPersonalInformation = agreeToPersonalInformation,
                isReceiveMarketing = isReceiveMarketing,
            )
        }
    }

    data class SignUpResponse(
        val job: Job,
        val interests: List<Interest>,
        val nickname: String,
        val birthday: String,
        val gender: Gender,
        val profileUrl: String,
        val phoneNumber: String,
        val email: String,
        val socialLoginType: SocialLoginType,
        val agreeToTermsOfServiceTermsOfUse: Boolean,
        val agreeToPersonalInformation: Boolean,
        val isReceiveMarketing: Boolean,
    ) {

        constructor(
            user: User
        ) : this(
            job = user.job,
            interests = user.interests.split(",").map{Interest(it)}.toList(),
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
        )
    }
}