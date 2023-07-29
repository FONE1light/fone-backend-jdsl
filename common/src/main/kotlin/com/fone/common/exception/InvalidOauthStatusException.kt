package com.fone.common.exception

data class InvalidOauthStatusException(override val message: String) : Exception(message)
