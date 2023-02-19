package com.fone.user.presentation

import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.SocialLoginType
import java.time.LocalDate

class RetrieveMyPageUserDto {

    data class RetrieveMyPageUserResponse(
        val user: UserDto,
    ) {

        constructor(
            user: User,
        ) : this(
            user = UserDto(user)
        )
    }
}