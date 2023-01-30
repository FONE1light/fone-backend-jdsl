package com.fone.home.presentation

data class HomeDto (
    val order: List<String>,
    val user: Any?,
    val jobOpenings: Any?,
    val competitions: Any?,
    val profiles: Any?
)