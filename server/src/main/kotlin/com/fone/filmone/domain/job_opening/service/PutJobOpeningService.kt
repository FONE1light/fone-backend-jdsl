package com.fone.filmone.domain.job_opening.service

import com.fone.filmone.common.exception.InvalidJobOpeningUserIdException
import com.fone.filmone.common.exception.NotFoundJobOpeningException
import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.RegisterJobOpeningRequest
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.RegisterJobOpeningResponse
import org.springframework.stereotype.Service

@Service
class PutJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val userRepository: UserRepository,
) {

    suspend fun putJobOpening(
        request: RegisterJobOpeningRequest,
        email: String,
        jobOpeningId: Long,
    ): RegisterJobOpeningResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()
        val jobOpening = jobOpeningRepository.findByTypeAndId(null, jobOpeningId)
            ?: throw NotFoundJobOpeningException()
        if (user.id != jobOpening.userId) {
            throw InvalidJobOpeningUserIdException()
        }

        jobOpening.put(request)

        jobOpeningRepository.save(jobOpening)

        return RegisterJobOpeningResponse(jobOpening)
    }
}