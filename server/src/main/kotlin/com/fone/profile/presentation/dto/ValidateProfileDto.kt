package com.fone.profile.presentation.dto

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.profile.presentation.dto.common.ProfileSnsUrl
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

class ValidateProfileDto {
    data class BasicPageValidation(
        @Schema(description = "프로필 이름", required = true, example = "차이나는 클라스")
        val name: String,
        @Schema(description = "후킹멘트", required = true, example = "제가 좋아하는 색은 노랑색이에요")
        val hookingComment: String,
        @Schema(
            description = "이미지 URL",
            example = "['https://s3-ap-northeast-2.amazonaws.com/f-one-image/prod/user-profile/image.jpg']"
        )
        val profileImages: List<String>,
    )

    data class DetailPageValidation(
        @Schema(description = "생년월일", example = "2000.10.01")
        val birthday: LocalDate,
        @Schema(description = "성별 (enum값에 성별무관 있음)", example = "WOMAN")
        val gender: Gender?,
        @Schema(description = "키 (스태프에서는 null, 유저 입력안할 시 -값으로 설정)", example = "188")
        val height: Int?,
        @Schema(description = "몸무게 (스태프에서는 null, 유저 입력안할 시 -값으로 설정)", example = "70")
        val weight: Int?,
        @Schema(description = "이메일", example = "example@something.com")
        val email: String,
        @Schema(description = "분야 (배우에서는 null)", example = "[\"PLANNING\",\"SCENARIO\"]")
        val domains: List<DomainType>?,
        @Schema(description = "특기", example = "매운 음식 먹기")
        val specialty: String,
        @Schema(
            description = "SNS link",
            example = "[{\"snsType\":\"INSTAGRAM\",\"url\":\"https://www.instagram.com/\"}]"
        )
        val snsUrls: List<ProfileSnsUrl>,
    )

    data class DescriptionPageValidation(
        @Schema(description = "상세요강")
        val details: String,
    )

    data class CareerPageValidation(
        @Schema(description = "경력", example = "LESS_THAN_3YEARS")
        val career: Career,
        @Schema(description = "경력 상세 설명", example = "복숭아 요거트 제작 3년")
        val careerDetail: String,
    )

    data class InterestPageValidation(
        @Schema(description = "관심사", example = "WEB_DRAMA")
        val categories: List<CategoryType>,
    )
}
