package com.fone.filmone.common.exception

sealed class ServerException(
    val code: Int,
    override val message: String,
) : RuntimeException(message)

data class UserNotFoundException(
    override val message: String = "유저가 존재하지 않습니다.",
) : ServerException(400, message)

data class NotFoundException(
    override val message: String,
) : ServerException(404, message)

data class InvalidTokenException(
    override val message: String = "유효하지 않는 토큰입니다.",
) : ServerException(401, message)