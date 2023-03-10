package com.fone.common.config

import com.fasterxml.classmate.TypeResolver
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
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.Response
import springfox.documentation.service.SecurityReference
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.Arrays

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
            .securityContexts(Arrays.asList(securityContext()))
            .securitySchemes(Arrays.asList<SecurityScheme>(apiKey()))
    }

    private fun setCommonResponse(): List<Response> {
        val list: MutableList<Response> = ArrayList()
        list.add(ResponseBuilder().code("200").description("?????? ??????(??????)").build())
        list.add(ResponseBuilder().code("401").description("?????? ?????? ?????? ????????? ?????? ?????? ?????? ??????").build())
        list.add(ResponseBuilder().code("404").description("???????????? ?????? api ?????? ").build())
        list.add(ResponseBuilder().code("500").description("?????? ?????? ??????(?????? ??????)").build())
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
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return Arrays.asList(SecurityReference("JWT", authorizationScopes))
    }

    private fun getConsumeContentTypes(): Set<String> {
        val consumes: MutableSet<String> = HashSet()
        consumes.add("application/json;charset=UTF-8")
        consumes.add("application/x-www-form-urlencoded")
        return consumes
    }

    private fun getProduceContentTypes(): Set<String> {
        val produces: MutableSet<String> = HashSet()
        produces.add("application/json;charset=UTF-8")
        return produces
    }

    private fun apiInfo(): ApiInfo? {
        return ApiInfoBuilder().title("Sig-Predict REST API Document")
            .description("work in progress").termsOfServiceUrl("localhost").version("1.0").build()
    }

    @ApiModel
    class PageModel {
        @ApiModelProperty(value = "????????? ??????(0..N)", example = "0")
        private val page: Int = 0

        @ApiModelProperty(value = "????????? ??????", allowableValues = "range[0, 100]", example = "0")
        private val size: Int = 0

        @ApiModelProperty(value = "??????(?????????: ?????????,ASC|DESC)")
        private val sort: List<String> = listOf()
    }
}
