package com.fone.filmone.application.user

import com.fone.filmone.domain.user.service.CheckNicknameDuplicateService
import com.fone.filmone.presentation.user.CheckNicknameDuplicateDto.CheckNicknameDuplicateRequest
import org.springframework.stereotype.Service

@Service
class CheckNicknameDuplicationFacade(
    private val checkNicknameDuplicateService: CheckNicknameDuplicateService,
) {
    suspend fun check(request: CheckNicknameDuplicateRequest) =
        checkNicknameDuplicateService.checkNicknameDuplicate(request)
}