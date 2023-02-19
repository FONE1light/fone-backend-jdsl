package com.fone.user.application

import com.fone.user.domain.service.CheckNicknameDuplicateService
import com.fone.user.presentation.CheckNicknameDuplicateDto.CheckNicknameDuplicateRequest
import org.springframework.stereotype.Service

@Service
class CheckNicknameDuplicationFacade(
    private val checkNicknameDuplicateService: CheckNicknameDuplicateService,
) {
    suspend fun check(request: CheckNicknameDuplicateRequest) =
        checkNicknameDuplicateService.checkNicknameDuplicate(request)
}