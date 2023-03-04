package com.fone.home.presentation

data class HomeDto(
    val order: List<String>,
    val jobOpening: CollectionDto,
    val competition: CollectionDto,
    val profile: CollectionDto,
)

data class CollectionDto(
    val title: String,
    val subTitle: String = "",
    val data: Any?,
)
