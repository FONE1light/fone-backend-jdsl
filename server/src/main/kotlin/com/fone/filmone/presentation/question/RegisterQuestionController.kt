package com.fone.filmone.presentation.question

import com.fone.filmone.application.question.RegisterQuestionFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.question.RegisterQuestionDto.RegisterQuestionRequest
import com.fone.filmone.presentation.question.RegisterQuestionDto.RegisterQuestionResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/user/v1/question")
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