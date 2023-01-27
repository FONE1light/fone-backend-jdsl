package com.fone.filmone.presentation.question

import com.fone.filmone.application.question.RegisterQuestionFacade
import com.fone.common.response.CommonResponse
import com.fone.filmone.presentation.question.RegisterQuestionDto.RegisterQuestionRequest
import com.fone.filmone.presentation.question.RegisterQuestionDto.RegisterQuestionResponse
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Api(tags = ["02. Question Info"], description = "문의등록 서비스")
@RestController
@RequestMapping("/api/v1/question")
class RegisterQuestionController(
    val registerQuestionFacade: RegisterQuestionFacade
) {

    @PostMapping
    suspend fun registerQuestion(@Valid @RequestBody request: RegisterQuestionRequest):
            CommonResponse<RegisterQuestionResponse> {
        val response = registerQuestionFacade.registerQuestion(request)
        return CommonResponse.success(response)
    }
}