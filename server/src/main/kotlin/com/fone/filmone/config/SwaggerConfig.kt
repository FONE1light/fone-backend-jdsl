package com.fone.filmone.config

import com.fasterxml.classmate.TypeResolver
import com.fone.competition.presentation.dto.RegisterCompetitionDto.RegisterCompetitionResponse
import com.fone.competition.presentation.dto.RetrieveCompetitionDto.RetrieveCompetitionResponse
import com.fone.competition.presentation.dto.RetrieveCompetitionDto.RetrieveCompetitionsResponse
import com.fone.competition.presentation.dto.RetrieveCompetitionScrapDto.RetrieveCompetitionScrapResponse
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto.RegisterJobOpeningResponse
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningResponse
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningsResponse
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningMyRegistrationDto.RetrieveJobOpeningMyRegistrationResponse
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningScrapDto.RetrieveJobOpeningScrapResponse
import com.fone.jobOpening.presentation.dto.RetrieveMySimilarJobOpeningDto.RetrieveMySimilarJobOpeningResponse
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileResponse
import com.fone.profile.presentation.dto.RetrieveProfileMyRegistrationDto.RetrieveProfileMyRegistrationResponse
import com.fone.profile.presentation.dto.RetrieveProfileWantDto.RetrieveProfileWantResponse
import com.fone.profile.presentation.dto.RetrieveProfilesDto.RetrieveProfileResponse
import com.fone.profile.presentation.dto.RetrieveProfilesDto.RetrieveProfilesResponse
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionResponse
import com.fone.report.presentation.dto.RegisterReportDto.RegisterReportResponse
import com.fone.sms.presentation.dto.SMSSendDto.SMSSendResponse
import com.fone.user.presentation.dto.CheckNicknameDuplicateDto.CheckNicknameDuplicateResponse
import com.fone.user.presentation.dto.EmailValidationDto.EmailDuplicationResponse
import com.fone.user.presentation.dto.EmailValidationDto.EmailValidationResponse
import com.fone.user.presentation.dto.ModifyUserDto.ModifyUserResponse
import com.fone.user.presentation.dto.PasswordValidationDto.PasswordValidationResponse
import com.fone.user.presentation.dto.RetrieveMyPageUserDto.RetrieveMyPageUserResponse
import com.fone.user.presentation.dto.SMSUserDto.PasswordSMSValidationResponse
import com.fone.user.presentation.dto.SMSUserDto.UserInfoSMSValidationResponse
import com.fone.user.presentation.dto.SignInUserDto.SignInUserResponse
import com.fone.user.presentation.dto.SignUpUserDto.SignUpUserResponse
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpMethod
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseBuilder
import springfox.documentation.schema.AlternateTypeRule
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.security.Principal

@Configuration
@EnableSwagger2
class SwaggerConfig(
    private val typeResolver: TypeResolver,
) {

    @Bean
    fun api(): Docket {
        val commonResponse = setCommonResponse()
        return Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
            .additionalModels(
                // competition
                typeResolver.resolve(RegisterCompetitionResponse::class.java),
                typeResolver.resolve(RetrieveCompetitionsResponse::class.java),
                typeResolver.resolve(RetrieveCompetitionResponse::class.java),
                typeResolver.resolve(RetrieveCompetitionScrapResponse::class.java),

                // jobOpening
                typeResolver.resolve(RegisterJobOpeningResponse::class.java),
                typeResolver.resolve(RetrieveJobOpeningsResponse::class.java),
                typeResolver.resolve(RetrieveJobOpeningResponse::class.java),
                typeResolver.resolve(RetrieveJobOpeningMyRegistrationResponse::class.java),
                typeResolver.resolve(RetrieveJobOpeningScrapResponse::class.java),
                typeResolver.resolve(RetrieveMySimilarJobOpeningResponse::class.java),

                // profile
                typeResolver.resolve(RegisterProfileResponse::class.java),
                typeResolver.resolve(RetrieveProfileMyRegistrationResponse::class.java),
                typeResolver.resolve(RetrieveProfilesResponse::class.java),
                typeResolver.resolve(RetrieveProfileResponse::class.java),
                typeResolver.resolve(RetrieveProfileWantResponse::class.java),

                // question
                typeResolver.resolve(RegisterQuestionResponse::class.java),

                // report
                typeResolver.resolve(RegisterReportResponse::class.java),

                // user
                typeResolver.resolve(CheckNicknameDuplicateResponse::class.java),
                typeResolver.resolve(ModifyUserResponse::class.java),
                typeResolver.resolve(RetrieveMyPageUserResponse::class.java),
                typeResolver.resolve(SignInUserResponse::class.java),
                typeResolver.resolve(SignUpUserResponse::class.java),
                typeResolver.resolve(PasswordValidationResponse::class.java),
                typeResolver.resolve(EmailDuplicationResponse::class.java),
                typeResolver.resolve(EmailValidationResponse::class.java),
                typeResolver.resolve(UserInfoSMSValidationResponse::class.java),
                typeResolver.resolve(PasswordSMSValidationResponse::class.java),

                // sms
                typeResolver.resolve(SMSSendResponse::class.java)
            )
            .globalResponses(HttpMethod.GET, commonResponse)
            .globalResponses(HttpMethod.POST, commonResponse)
            .globalResponses(HttpMethod.PUT, commonResponse)
            .globalResponses(HttpMethod.PATCH, commonResponse)
            .globalResponses(HttpMethod.DELETE, commonResponse).alternateTypeRules(
                AlternateTypeRule(
                    typeResolver.resolve(Pageable::class.java),
                    typeResolver.resolve(PageModel::class.java)
                )
            ).consumes(getConsumeContentTypes()).produces(getProduceContentTypes())
            .apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.fone"))
            .paths(PathSelectors.ant("/**")).build()
            .ignoredParameterTypes(Principal::class.java)
            .securityContexts(listOf(securityContext()))
            .securitySchemes(listOf<SecurityScheme>(apiKey()))
    }

    private fun setCommonResponse() = listOf(
        ResponseBuilder().code("200").description("정상 처리(성공)").build(),
        ResponseBuilder().code("401").description("토큰 만료 또는 비정상 토큰 또는 권한 없음").build(),
        ResponseBuilder().code("404").description("존재하지 않는 api 요청 ").build(),
        ResponseBuilder().code("500").description("내부 서버 오류(문의 필요)").build()
    )

    private fun apiKey(): ApiKey {
        return ApiKey("JWT", "Authorization", "header")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder().securityReferences(defaultAuth())
            .forPaths(PathSelectors.any()).build()
    }

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return listOf(SecurityReference("JWT", authorizationScopes))
    }

    private fun getConsumeContentTypes(): Set<String> = setOf(
        "application/json;charset=UTF-8",
        "application/x-www-form-urlencoded"
    )

    private fun getProduceContentTypes(): Set<String> = setOf("application/json;charset=UTF-8")

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder().title("REST API Document.")
            .description("work in progress").termsOfServiceUrl("localhost").version("1.0").build()
    }

    @ApiModel
    class PageModel {
        @ApiModelProperty(value = "페이지 번호(0..N)", example = "0")
        val page: Int = 0

        @ApiModelProperty(value = "페이지 크기", allowableValues = "range[0, 100]", example = "0")
        val size: Int = 0

        @ApiModelProperty(value = "정렬(사용법: 컬럼명,ASC|DESC)")
        val sort: List<String> = listOf()
    }
}
