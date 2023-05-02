package com.fone.common.exception

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(
        request: ServerRequest?,
        options: ErrorAttributeOptions?,
    ): Map<String, Any?> {
        val map: MutableMap<String, Any?> = mutableMapOf()
        val throwable: Throwable = getError(request)
        return if (throwable is GlobalException) {
            val ex: GlobalException = getError(request) as GlobalException
            map.apply {
                this["result"] = "FAIL"
                this["data"] = null
                this["message"] = ex.reason
                this["errorCode"] = ex.status.reasonPhrase
            }
        } else {
            map.apply {
                this["result"] = "FAIL"
                this["data"] = null
                this["message"] = throwable.toString()
                this["errorCode"] = null
            }
        }
    }
}
