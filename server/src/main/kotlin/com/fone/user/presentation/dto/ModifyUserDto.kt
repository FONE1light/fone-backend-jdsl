package com.fone.user.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.jwt.Role
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.Job
import com.fone.user.presentation.dto.common.UserDto
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class ModifyUserDto {

    data class ModifyUserRequest(
        @field:NotNull(message = "직업은 필수 값 입니다.") val job: Job,
        @field:Size(min = 1, message = "관심사는 1개 이상 선택 되어야 합니다") val interests: List<CategoryType>,
        @field:NotEmpty(message = "닉네임은 필수 값 입니다.") val nickname: String,
        val profileUrl: String?,
    )

    data class AdminModifyUserRequest(
        val job: Job? = null,
        val interests: List<CategoryType>? = null,
        val nickname: String? = null,
        val profileUrl: String? = null,
        val roles: List<Role>? = null,
    )
    data class ModifyUserResponse(
        val user: UserDto,
    ) {
        constructor(
            user: User,
        ) : this(user = UserDto(user))
    }
}
