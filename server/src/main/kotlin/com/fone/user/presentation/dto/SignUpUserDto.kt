package com.fone.user.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.common.jwt.Role
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.SocialLoginType
import com.fone.user.presentation.dto.common.UserDto
import io.swagger.annotations.ApiModelProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class SignUpUserDto {

    data class SignUpUserRequest(
        @field:NotNull(message = "직업은 필수 값 입니다.") val job: Job,
        @field:Size(min = 1, message = "관심사는 1개 이상 선택 되어야 합니다") val interests: List<CategoryType>,
        @field:NotEmpty(message = "닉네임은 필수 값 입니다.") val nickname: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd") val birthday: LocalDate,
        @field:NotNull(message = "성별은 필수 값 입니다.") val gender: Gender,
        val profileUrl: String?,
        @field:Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}\$")
        @ApiModelProperty(
            value = "휴대폰 번호",
            example = "010-1234-1234",
            required = true
        )
        val phoneNumber: String,
        @field:NotEmpty(message = "이메일은 필수 값 입니다.")
        @field:Email(message = "유효하지 않는 이메일 입니다.")
        @ApiModelProperty(
            value = "이메일",
            example = "test@test.com",
            required = true
        )
        val email: String,
        @field:NotNull(message = "소셜 로그인 타입은 필수 값 입니다.") val socialLoginType: SocialLoginType,
        @field:AssertTrue(message = "이용약관 동의 선택은 필수 값 입니다.") val agreeToTermsOfServiceTermsOfUse: Boolean,
        @field:AssertTrue(message = "개인정보 취급방침 동의 선택은 필수 값 입니다.") val agreeToPersonalInformation: Boolean,
        @field:NotNull(message = "마케팅 정보수신 동의는 필수 값 입니다.") val isReceiveMarketing: Boolean,
        @field:NotEmpty(message = "액세스 토큰은 필수 값 입니다.") val accessToken: String,
    ) {
        fun toEntity(): User {
            return User(
                job = job,
                interests = interests.map { it.toString() }.toList(),
                nickname = nickname,
                birthday = birthday,
                gender = gender,
                profileUrl = profileUrl ?: "",
                phoneNumber = phoneNumber,
                email = email,
                socialLoginType = socialLoginType,
                agreeToTermsOfServiceTermsOfUse = agreeToTermsOfServiceTermsOfUse,
                agreeToPersonalInformation = agreeToPersonalInformation,
                isReceiveMarketing = isReceiveMarketing,
                roles = listOf(Role.ROLE_USER).map { it.toString() }.toList(),
                enabled = true
            )
        }
    }

    data class SignUpUserResponse(
        val user: UserDto,
    ) {

        constructor(
            user: User,
        ) : this(user = UserDto(user))
    }
}
