package com.fone.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class GlobalException : ResponseStatusException {
    constructor(status: HttpStatus, reason: String) : super(status, reason)

    companion object {
        private const val serialVersionUID = -1L
    }
}
