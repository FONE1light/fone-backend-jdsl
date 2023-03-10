package com.fone.user.presentation.dto

import com.fone.user.presentation.dto.common.UserDto

class RetrieveMyPageUserDto {

    data class RetrieveMyPageUserResponse(
        val user: UserDto,
    ) {

        constructor(
            user: com.fone.user.domain.entity.User,
        ) : this(user = UserDto(user))
    }
}
