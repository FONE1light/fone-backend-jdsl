package com.fone.filmone.application.profile

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.profile.service.RetrieveProfileWantService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveProfileWantFacade(
    private val retrieveProfileWantService: RetrieveProfileWantService,
) {

    suspend fun retrieveProfileWant(pageable: Pageable, email: String, type: Type) =
        retrieveProfileWantService.retrieveProfileWant(pageable, email, type)
}