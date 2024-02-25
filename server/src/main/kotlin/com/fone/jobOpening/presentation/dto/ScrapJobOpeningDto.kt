package com.fone.jobOpening.presentation.dto

data class ScrapJobOpeningResponse(
    val result: ScrapJobResult,
)

enum class ScrapJobResult {
    SCRAPPED, DISCARDED
}
