package com.fone.question.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.question.application.RegisterQuestionFacade
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionRequest
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@Api(tags = ["02. Question Info"], description = "문의등록 서비스")
@RestController
@RequestMapping("/api/v1/questions")
class RegisterQuestionController(
    val registerQuestionFacade: RegisterQuestionFacade,
) {

    @PostMapping
    @ApiOperation(value = "비회원 문의등록 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RegisterQuestionResponse::class))]
    )
    suspend fun registerQuestion(
        @Valid @RequestBody
        request: RegisterQuestionRequest,
    ): CommonResponse<RegisterQuestionResponse> {
        val response = registerQuestionFacade.registerQuestion(request)
        return CommonResponse.success(response)
    }

    @PostMapping("/user")
    @ApiOperation(value = "회원 문의등록 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RegisterQuestionResponse::class))]
    )
    suspend fun registerQuestionWithPrincipal(
        @Valid @RequestBody
        request: RegisterQuestionRequest,
        principal: Principal,
    ): CommonResponse<RegisterQuestionResponse> {
        val response = registerQuestionFacade.registerQuestion(principal.name, request)
        return CommonResponse.success(response)
    }
}
