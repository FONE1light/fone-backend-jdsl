package com.fone.filmone.common.config

import com.fasterxml.classmate.TypeResolver
import com.fone.filmone.presentation.job_opening.*
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.*
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningDto.*
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningMyRegistrationDto.*
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningScrapDto.*
import com.fone.filmone.presentation.job_opening.RetrieveMySimilarJobOpeningDto.*
import com.fone.filmone.presentation.profile.RegisterProfileDto
import com.fone.filmone.presentation.profile.RegisterProfileDto.*
import com.fone.filmone.presentation.profile.RetrieveProfileMyRegistrationDto
import com.fone.filmone.presentation.profile.RetrieveProfileMyRegistrationDto.*
import com.fone.filmone.presentation.profile.RetrieveProfileWantDto
import com.fone.filmone.presentation.profile.RetrieveProfileWantDto.*
import com.fone.filmone.presentation.profile.RetrieveProfilesDto
import com.fone.filmone.presentation.profile.RetrieveProfilesDto.*
import com.fone.filmone.presentation.question.RegisterQuestionDto
import com.fone.filmone.presentation.question.RegisterQuestionDto.*
import com.fone.filmone.presentation.user.CheckNicknameDuplicateDto.CheckNicknameDuplicateResponse
import com.fone.filmone.presentation.user.ModifyUserDto.ModifyUserResponse
import com.fone.filmone.presentation.user.RetrieveMyPageUserDto.RetrieveMyPageUserResponse
import com.fone.filmone.presentation.user.SignUpUserDto
import com.fone.filmone.presentation.user.SignUpUserDto.*
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpMethod
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseBuilder
import springfox.documentation.schema.AlternateTypeRule
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
class SwaggerConfig(
    private val typeResolver: TypeResolver,
) {

    @Bean
    fun api(): Docket? {
        val commonResponse = setCommonResponse()
        return Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
            .additionalModels(
                typeResolver.resolve(CheckNicknameDuplicateResponse::class.java),
                typeResolver.resolve(ModifyUserResponse::class.java),
                typeResolver.resolve(RetrieveMyPageUserResponse::class.java),
                typeResolver.resolve(SignUpUserResponse::class.java),
                typeResolver.resolve(RegisterQuestionResponse::class.java),
                typeResolver.resolve(RegisterJobOpeningResponse::class.java),
                typeResolver.resolve(RetrieveJobOpeningsResponse::class.java),
                typeResolver.resolve(RetrieveJobOpeningResponse::class.java),
                typeResolver.resolve(RetrieveJobOpeningMyRegistrationResponse::class.java),
                typeResolver.resolve(RetrieveJobOpeningScrapResponse::class.java),
                typeResolver.resolve(RetrieveMySimilarJobOpeningResponse::class.java),
                typeResolver.resolve(RegisterProfileResponse::class.java),
                typeResolver.resolve(RetrieveProfileMyRegistrationResponse::class.java),
                typeResolver.resolve(RetrieveProfilesResponse::class.java),
                typeResolver.resolve(RetrieveProfileResponse::class.java),
                typeResolver.resolve(RetrieveProfileWantResponse::class.java),
            )
            .globalResponses(HttpMethod.GET, commonResponse)
            .globalResponses(HttpMethod.POST, commonResponse)
            .globalResponses(HttpMethod.PUT, commonResponse)
            .globalResponses(HttpMethod.PATCH, commonResponse)
            .globalResponses(HttpMethod.DELETE, commonResponse)
            .alternateTypeRules(
                AlternateTypeRule(
                    typeResolver.resolve(Pageable::class.java),
                    typeResolver.resolve(Page::class.java)
                )
            )
            .consumes(getConsumeContentTypes())
            .produces(getProduceContentTypes()).apiInfo(apiInfo()).select()
            .apis(RequestHandlerSelectors.basePackage("com.fone"))
            .paths(PathSelectors.ant("/**")).build()
            .securityContexts(Arrays.asList(securityContext()))
            .securitySchemes(Arrays.asList<SecurityScheme>(apiKey()))
    }

    private fun setCommonResponse(): List<Response> {
        val list: MutableList<Response> = ArrayList()
        list.add(ResponseBuilder().code("200").description("정상 처리(성공)").build())
        list.add(
            ResponseBuilder().code("401").description("토큰 만료 또는 비정상 토큰 또는 권한 없음").build()
        )
        list.add(ResponseBuilder().code("404").description("존재하지 않는 api 요청 ").build())
        list.add(ResponseBuilder().code("500").description("내부 서버 오류(문의 필요)").build())
        return list
    }

    private fun apiKey(): ApiKey {
        return ApiKey("JWT", "Authorization", "header")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder().securityReferences(defaultAuth())
            .forPaths(PathSelectors.any()).build()
    }

    private fun defaultAuth(): List<SecurityReference>? {
        val authorizationScope = AuthorizationScope(
            "global", "accessEverything"
        )
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return Arrays.asList(SecurityReference("JWT", authorizationScopes))
    }

    private fun getConsumeContentTypes(): Set<String>? {
        val consumes: MutableSet<String> = HashSet()
        consumes.add("application/json;charset=UTF-8")
        consumes.add("application/x-www-form-urlencoded")
        return consumes
    }

    private fun getProduceContentTypes(): Set<String>? {
        val produces: MutableSet<String> = HashSet()
        produces.add("application/json;charset=UTF-8")
        return produces
    }

    private fun apiInfo(): ApiInfo? {
        return ApiInfoBuilder().title("Sig-Predict REST API Document")
            .description("work in progress").termsOfServiceUrl("localhost").version("1.0").build()
    }

    @ApiModel
    class Page {
        @ApiModelProperty(value = "페이지 번호(0..N)", example = "0")
        private val page: Int = 0

        @ApiModelProperty(value = "페이지 크기", allowableValues = "range[0, 100]", example = "0")
        private val size: Int = 0

        @ApiModelProperty(value = "정렬(사용법: 컬럼명,ASC|DESC)")
        private val sort: List<String> = listOf()
    }
}
