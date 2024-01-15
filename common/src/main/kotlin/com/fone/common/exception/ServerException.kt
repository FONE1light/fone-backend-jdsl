package com.fone.common.exception

sealed class ServerException(
    val code: Int,
    override val message: String,
) : RuntimeException(message)

data class DuplicateUserException(
    override val message: String = "이미 존재 하는 이메일 혹은 닉네임 입니다.",
) : ServerException(400, message)

data class DuplicateUserNicknameException(
    override val message: String = "중복되는 닉네임입니다",
) : ServerException(400, message)

data class DuplicatePhoneNumberException(
    override val message: String = "이미 이 휴대폰 번호로 가입된 회원입니다.",
) : ServerException(400, message)

data class NotFoundUserException(override val message: String = "존재 하지 않는 유저 입니다.") : ServerException(400, message)

data class NotFoundJobOpeningException(override val message: String = "존재 하지 않는 모집 입니다.") :
    ServerException(400, message)

data class InvalidJobOpeningUserIdException(
    override val message: String = "구인구직을 등록한 유저만 수정할 수 있습니다.",
) : ServerException(403, message)

data class NotFoundProfileException(override val message: String = "존재 하지 않는 프로필 입니다.") : ServerException(400, message)

data class InvalidProfileUserIdException(
    override val message: String = "프로필을 등록한 유저만 수정할 수 있습니다.",
) : ServerException(403, message)

data class NotFoundCompetitionException(override val message: String = "존재 하지 않는 공모전 입니다.") :
    ServerException(400, message)

data class NotFoundException(
    override val message: String,
) : ServerException(404, message)

data class InvalidTokenException(
    override val message: String = "유효하지 않은 토큰입니다.",
) : ServerException(401, message)

data class ForbiddenException(
    override val message: String = "금지된 요청입니다.",
) : ServerException(403, message)

data class InvalidUserStateException(
    override val message: String = "유효하지 않은 토큰입니다.",
) : ServerException(400, message)

data class InvalidOauthStatusException(
    override val message: String,
) : ServerException(400, message)

data class SMSBackendException(
    override val message: String = "SMS 인증에 오류가 있음.",
) : ServerException(500, message)

data class SMSValidationException(
    override val message: String = "인증번호가 적절하지 않음.",
) : ServerException(400, message)

data class EmailBackendException(
    override val message: String = "이메일 전송 오류 발생",
) : ServerException(500, message)

data class BadLocationException(
    override val message: String = "Location 정보가 유효하지 않음.",
) : ServerException(400, message)

data class RequestValidationException(
    override val message: String,
) : ServerException(400, message)
