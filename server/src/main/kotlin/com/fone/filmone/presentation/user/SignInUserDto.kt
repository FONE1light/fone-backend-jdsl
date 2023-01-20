package com.fone.filmone.presentation.user

import com.fone.filmone.domain.user.Token
import com.fone.filmone.domain.user.entity.User
import com.fone.filmone.domain.user.enum.SocialLoginType
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class SignInUserDto {

    data class SignInUserRequest(
        @field:NotNull(message = "소셜로그인 타입은 필수 값 입니다.")
        val socialLoginType: SocialLoginType,
        @field:NotEmpty(message = "이메일은 필수 값 입니다.")
        @field:Email(message = "유효하지 않는 이메일 입니다.")
        @ApiModelProperty(value = "이메일", example = "test@test.com", required = true)
        val email: String,
        @field:NotEmpty(message = "accessToken은 필수 값 입니다.")
        val accessToken: String,
    )

    data class SignInUserResponse(
        val socialLoginType: SocialLoginType,
        val email: String,
        val token: Token,
    ) {

        constructor(
            user: User,
            token: Token,
        ) : this(
            socialLoginType = user.socialLoginType,
            email = user.email,
            token = token,
        )
    }
}