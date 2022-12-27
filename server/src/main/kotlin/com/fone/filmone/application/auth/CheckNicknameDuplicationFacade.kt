package com.fone.filmone.application.auth

import com.fone.filmone.domain.user.service.RetrieveUserService
import com.fone.filmone.presentation.auth.CheckNicknameDuplicateDto.CheckNicknameDuplicateRequest
import org.springframework.stereotype.Service

@Service
class CheckNicknameDuplicationFacade(
    private val retrieveUserService: RetrieveUserService,
) {
    suspend fun check(request: CheckNicknameDuplicateRequest) =
        retrieveUserService.checkNicknameDuplicate(request)
}