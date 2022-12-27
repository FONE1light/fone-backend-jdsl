package com.fone.filmone.application.auth

import com.fone.filmone.domain.user.service.UserRetrieveService
import com.fone.filmone.presentation.auth.CheckNicknameDuplicateDto.CheckNicknameDuplicateRequest
import org.springframework.stereotype.Service

@Service
class CheckNicknameDuplicationFacade(
    private val retrieveUserService: UserRetrieveService,
) {
    suspend fun check(request: CheckNicknameDuplicateRequest) =
        retrieveUserService.checkNicknameDuplicate(request)
}