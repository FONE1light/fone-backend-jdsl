package com.fone.common.exception

import org.springframework.http.HttpStatus

class UnauthorizedException : GlobalException {
    constructor(status: HttpStatus, reason: String) : super(status, reason)

    companion object {
        private const val serialVersionUID = -1L
    }
}
