package com.fone.filmone.presentation.auth

import com.fone.filmone.domain.user.entity.User
import com.fone.filmone.domain.user.enum.Gender
import com.fone.filmone.domain.user.enum.Interest
import com.fone.filmone.domain.user.enum.Job
import com.fone.filmone.domain.user.enum.SocialLoginType
import org.springframework.format.annotation.DateTimeFormat
import java.sql.Date
import javax.validation.constraints.*

class SignUpDto {

    data class SignUpRequest(
        @field:NotNull(message = "직업은 필수 값 입니다.")
        val job: Job,
        @field:Size(min = 1, message = "관심사는 1개 이상 선택 되어야 합니다")
        val interests: List<Interest>,
        @field:NotEmpty(message = "닉네임은 필수 값 입니다.")
        val nickname: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val birthday: Date,
        @field:NotNull(message = "성별은 필수 값 입니다.")
        val gender: Gender,
        val profileUrl: String?,
        @field:Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}\$")
        val phoneNumber: String,
        @field:NotEmpty(message = "이메일은 필수 값 입니다.")
        @field:Email(message = "유효하지 않는 이메일 입니다.")
        val email: String,
        @field:NotNull(message = "소셜 로그인 타입은 필수 값 입니다.")
        val socialLoginType: SocialLoginType,
        @field:AssertTrue(message = "이용약관 동의 선택은 필수 값 입니다.")
        val agreeToTermsOfServiceTermsOfUse: Boolean,
        @field:AssertTrue(message = "개인정보 취급방침 동의 선택은 필수 값 입니다.")
        val agreeToPersonalInformation: Boolean,
        @field:NotNull(message = "마케팅 정보수신 동의는 필수 값 입니다.")
        val isReceiveMarketing: Boolean,
        @field:NotEmpty(message = "액세스 토큰은 필수 값 입니다.")
        val accessToken: String,
    ) {
        fun toEntity(): User {

            return User(
                job = job,
                interests = interests.joinToString(","),
                nickname = nickname,
                birthday = birthday.toString(),
                gender = gender,
                profileUrl = profileUrl ?: "",
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