package com.fone.jobOpening.presentation.dto

class ScrapJobOpeningDto {
    data class ScrapJobOpeningResponse(
        val result: ScrapJobResult,
    )

    enum class ScrapJobResult {
        SCRAPPED, DISCARDED
    }
}
