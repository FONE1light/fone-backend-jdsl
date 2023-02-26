package com.fone.common.redis

import java.util.concurrent.TimeUnit
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisRepository(
    val redisTemplate: RedisTemplate<String, String>,
) {

    val REFRESH_PREFIX: String = "RT:"

    fun setValue(key: String, value: String, timeout: Long, unit: TimeUnit) {
        val values = redisTemplate.opsForValue()
        values.set(key, value, timeout, unit)
    }

    fun getValue(key: String): String? {
        val values = redisTemplate.opsForValue()
        return values.get(key)
    }

    fun delValue(key: String) {
        redisTemplate.delete(key)
    }
}
