package com.fone.user.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.common.jwt.Role
import com.fone.common.password.PasswordService
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.LoginType
import com.fone.user.presentation.dto.common.UserDto
import io.swagger.annotations.ApiModelProperty
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.server.ServerWebInputException
import java.time.LocalDate
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class SignUpUserDto {

    data class SocialSignUpUserRequest(
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
        val identifier: String? = null,
        @field:NotNull(message = "로그인 타입은 필수 값 입니다.")
        val loginType: LoginType,
        @field:AssertTrue(message = "이용약관 동의 선택은 필수 값 입니다.") val agreeToTermsOfServiceTermsOfUse: Boolean,
        @field:AssertTrue(message = "개인정보 취급방침 동의 선택은 필수 값 입니다.") val agreeToPersonalInformation: Boolean,
        @field:NotNull(message = "마케팅 정보수신 동의는 필수 값 입니다.") val isReceiveMarketing: Boolean,
        @ApiModelProperty(value = "소셜 인증 토큰") val accessToken: String,
    ) {
        fun toEntity(): User {
            loginTypeAssertion()
            val identifier = when (loginType) {
                LoginType.APPLE -> identifier
                else -> email
            } ?: throw ServerWebInputException("애플의 경우 identifier가 명시되어 있어야함")
            return User(
                job = job,
                interests = interests.map { it.toString() },
                nickname = nickname,
                birthday = birthday,
                gender = gender,
                profileUrl = profileUrl ?: "",
                phoneNumber = phoneNumber,
                email = email,
                identifier = identifier,
                loginType = loginType,
                agreeToTermsOfServiceTermsOfUse = agreeToTermsOfServiceTermsOfUse,
                agreeToPersonalInformation = agreeToPersonalInformation,
                isReceiveMarketing = isReceiveMarketing,
                roles = listOf(Role.ROLE_USER).map { it.toString() },
                enabled = true
            )
        }
        private fun loginTypeAssertion() {
            if (LoginType.PASSWORD == loginType) {
                throw ServerWebInputException("Password 로그인의 경우 Password 있어야함")
            }
        }
    }

    data class EmailSignUpUserRequest(
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
        val identifier: String? = null,
        @field:AssertTrue(message = "이용약관 동의 선택은 필수 값 입니다.") val agreeToTermsOfServiceTermsOfUse: Boolean,
        @field:AssertTrue(message = "개인정보 취급방침 동의 선택은 필수 값 입니다.") val agreeToPersonalInformation: Boolean,
        @field:NotNull(message = "마케팅 정보수신 동의는 필수 값 입니다.") val isReceiveMarketing: Boolean,
        @field:Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "영문자, 숫자, 특수문자가 포함된 8~16자 비밀번호"
        ) val password: String,
        @ApiModelProperty(value = "이메일 인증 토큰", required = true) val token: String,
    ) {
        fun toEntity(): User {
            return User(
                job = job,
                interests = interests.map { it.toString() },
                nickname = nickname,
                birthday = birthday,
                gender = gender,
                profileUrl = profileUrl ?: "",
                phoneNumber = phoneNumber,
                email = email,
                identifier = identifier,
                loginType = LoginType.PASSWORD,
                agreeToTermsOfServiceTermsOfUse = agreeToTermsOfServiceTermsOfUse,
                agreeToPersonalInformation = agreeToPersonalInformation,
                isReceiveMarketing = isReceiveMarketing,
                roles = listOf(Role.ROLE_USER).map { it.toString() },
                enabled = true,
                password = password.run(PasswordService::hashPassword)
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
