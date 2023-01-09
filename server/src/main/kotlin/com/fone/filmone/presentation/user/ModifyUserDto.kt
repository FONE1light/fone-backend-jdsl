package com.fone.filmone.presentation.user

import com.fone.filmone.domain.user.entity.User
import com.fone.filmone.domain.user.enum.Gender
import com.fone.filmone.domain.user.enum.Interest
import com.fone.filmone.domain.user.enum.Job
import com.fone.filmone.domain.user.enum.SocialLoginType
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class ModifyUserDto {

    data class ModifyUserRequest (
        @field:NotNull(message = "직업은 필수 값 입니다.")
        val job: Job,
        @field:Size(min = 1, message = "관심사는 1개 이상 선택 되어야 합니다")
        val interests: List<Interest>,
        @field:NotEmpty(message = "닉네임은 필수 값 입니다.")
        val nickname: String,
        @field:Email(message = "유효하지 않는 이메일 입니다.")
        val email: String,
    )

    data class ModifyUserResponse(
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
            interests = user.interests.split(",").map { Interest(it) }.toList(),
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