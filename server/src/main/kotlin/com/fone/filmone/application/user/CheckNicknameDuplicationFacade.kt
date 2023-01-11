package com.fone.filmone.application.user

import com.fone.filmone.domain.user.service.RetrieveUserService
import com.fone.filmone.presentation.user.CheckNicknameDuplicateDto.CheckNicknameDuplicateRequest
import org.springframework.stereotype.Service

@Service
class CheckNicknameDuplicationFacade(
    private val retrieveUserService: RetrieveUserService,
) {
    suspend fun check(request: CheckNicknameDuplicateRequest) =
        retrieveUserService.checkNicknameDuplicate(request)
}