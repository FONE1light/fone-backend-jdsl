package com.fone.filmone.presentation.profile

import com.fone.filmone.domain.common.*
import com.fone.filmone.domain.profile.entity.Profile
import org.springframework.format.annotation.DateTimeFormat
import java.sql.Date
import javax.validation.constraints.Email

class RegisterProfileDto {

    data class RegisterProfileRequest(
        val hookingComment: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val birthday: Date,
        val gender: Gender,
        val height: Int,
        val weight: Int,
        @field:Email(message = "유효하지 않는 이메일 입니다.")
        val email: String,
        val sns: String,
        val specialty: String,
        val details: String,
        val career: Career,
        val interests: List<Interest>,
        val type: Type,
        val domains: List<Domain>,
        val userId: Long,
        val profileUrls: List<String>,
    ) {

        fun toEntity(userId: Long): Profile {

            return Profile(
                hookingComment = hookingComment,
                birthday = birthday.toString(),
                gender = gender,
                height = height,
                weight = weight,
                email = email,
                sns = sns,
                specialty = specialty,
                details = details,
                career = career,
                interests = interests.joinToString(","),
                type = type,
                domains = domains.joinToString(","),
                userId = userId,
            )
        }
    }

    data class RegisterProfileResponse(
        val hookingComment: String,
    ) {

        constructor(
            profile: Profile
        ) : this(
            hookingComment = profile.hookingComment
        )
    }
}