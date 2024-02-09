package com.fone.jobOpening.domain.service

import com.fone.jobOpening.domain.repository.LocationRepository
import com.fone.jobOpening.presentation.dto.LocationDto.RetrieveDistrictsResponse
import com.fone.jobOpening.presentation.dto.LocationDto.RetrieveRegionsResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LocationService(
    private val locationRepository: LocationRepository,
) {
    @Transactional(readOnly = true)
    suspend fun retrieveRegions(): RetrieveRegionsResponse {
        val result = locationRepository.getAllRegions()
        return RetrieveRegionsResponse(
            regions = result
        )
    }

    @Transactional(readOnly = true)
    suspend fun retrieveDistricts(region: String): RetrieveDistrictsResponse {
        val result = locationRepository.getDistricts(region)
        return RetrieveDistrictsResponse(
            districts = result
        )
    }
}
