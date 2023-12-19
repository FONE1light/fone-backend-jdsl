package com.fone.jobOpening.domain.repository

import com.fone.jobOpening.domain.entity.Location

interface LocationRepository {
    suspend fun saveAll(locations: Collection<Location>): Collection<Location>

    suspend fun findLocation(
        region: String,
        district: String,
    ): Location

    suspend fun getAllRegions(): List<String>

    suspend fun getDistricts(region: String): List<String>

    suspend fun count(): Long
}
