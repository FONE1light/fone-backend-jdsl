package com.fone.filmone.common.exception

sealed class ServerException(
    val code: Int,
    override val message: String,
) : RuntimeException(message)

data class DuplicateUserException(
    override val message: String = "이미 존재 하는 이메일 혹은 닉네임 입니다.",
) : ServerException(200, message)

data class NotFoundUserException(
    override val message: String = "존재 하지 않는 유저 입니다."
) : ServerException(200, message)

data class NotFoundJobOpeningException(
    override val message: String = "존재 하지 않는 모집 입니다."
) : ServerException(200, message)

data class InvalidJobOpeningUserIdException(
    override val message: String = "구인구직을 등록한 유저만 수정할 수 있습니다."
) : ServerException(403, message)

data class NotFoundProfileException(
    override val message: String = "존재 하지 않는 프로필 입니다."
) : ServerException(200, message)

data class InvalidProfileUserIdException(
    override val message: String = "프로필을 등록한 유저만 수정할 수 있습니다."
) : ServerException(403, message)

data class NotFoundException(
    override val message: String,
) : ServerException(404, message)