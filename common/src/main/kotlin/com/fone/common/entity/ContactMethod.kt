package com.fone.common.entity

enum class ContactMethod(contactMethod: String) {
    KAKAO("카카오 오픈채팅"),
    EMAIL("이메일"),
    GOOGLE_FORM("구글 폼"),
    ;

    companion object {
        operator fun invoke(contactMethod: String) = ContactMethod.valueOf(contactMethod.uppercase())

        fun getAllEnum(): List<ContactMethod> {
            return listOf(
                KAKAO,
                EMAIL,
                GOOGLE_FORM
            )
        }
    }
}
