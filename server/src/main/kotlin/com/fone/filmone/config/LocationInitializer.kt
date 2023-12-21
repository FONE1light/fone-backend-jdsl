package com.fone.filmone.config

import com.fone.jobOpening.domain.entity.Location
import com.fone.jobOpening.domain.repository.LocationRepository
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class LocationInitializer(val locationRepository: LocationRepository) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        runBlocking {
            if (locationRepository.count() == 0L) {
                val locations =
                    getLocationCsv().split("\n")
                        .filter { it.isNotBlank() }
                        .map {
                            val (region, district) = it.split(",")
                            Location(region = region, district = district)
                        }
                locationRepository.saveAll(locations)
            }
        }
    }

    private fun getLocationCsv(): String {
        val classLoader = LocationInitializer::class.java.classLoader
        val stream = classLoader.getResourceAsStream("locations.csv")!!
        return String(stream.readAllBytes())
    }
}
