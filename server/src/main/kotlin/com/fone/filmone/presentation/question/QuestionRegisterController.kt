package com.fone.filmone.presentation.question

import com.fone.filmone.application.question.QuestionRegisterFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.question.QuestionRegisterDto.QuestionRegisterRequest
import com.fone.filmone.presentation.question.QuestionRegisterDto.QuestionRegisterResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/user/v1/question")
class QuestionRegisterController(
    val questionRegisterFacade: QuestionRegisterFacade
) {

    @PostMapping
    suspend fun registerQuestion(@Valid @RequestBody request: QuestionRegisterRequest):
            CommonResponse<QuestionRegisterResponse> {
        val response = questionRegisterFacade.registerQuestion(request)
        return CommonResponse.success(response)
    }
}