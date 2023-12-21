package com.fone.jobOpening.application

import com.fone.jobOpening.domain.service.LocationService
import org.springframework.stereotype.Service

@Service
class LocationFacade(
    private val locationService: LocationService,
) {
    suspend fun retrieveRegions() = locationService.retrieveRegions()

    suspend fun retrieveDistricts(region: String) = locationService.retrieveDistricts(region)
}
