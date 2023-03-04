package com.fone.profile.application

import com.fone.common.entity.Type
import com.fone.profile.domain.service.RetrieveProfileWantService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveProfileWantFacade(
    private val retrieveProfileWantService: RetrieveProfileWantService
) {

    suspend fun retrieveProfileWant(pageable: Pageable, email: String, type: Type) =
        retrieveProfileWantService.retrieveProfileWant(pageable, email, type)
}
