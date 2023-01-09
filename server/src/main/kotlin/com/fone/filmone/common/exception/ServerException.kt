package com.fone.filmone.common.exception

sealed class ServerException(
    val code: Int,
    override val message: String,
) : RuntimeException(message)

data class UserNotFoundException(
    override val message: String = "유저가 존재하지 않습니다.",
) : ServerException(400, message)

data class DuplicateUserException(
    override val message: String = "이미 존재 하는 이메일 혹은 닉네임 입니다.",
) : ServerException(200, message)

data class NotFoundUserException(
    override val message: String = "존재 하지 않는 유저 입니다."
) : ServerException(200, message)

data class NotFoundException(
    override val message: String,
) : ServerException(404, message)

data class InvalidTokenException(
    override val message: String = "유효하지 않는 토큰입니다.",
) : ServerException(401, message)