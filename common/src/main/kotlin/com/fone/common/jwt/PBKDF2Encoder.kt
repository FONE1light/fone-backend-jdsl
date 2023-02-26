package com.fone.common.jwt

import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PBKDF2Encoder : PasswordEncoder {
    @Value("\${security.password.encoder.secret}") private lateinit var secret: String

    @Value("\${security.password.encoder.iteration}") private val iteration: Int = 0

    @Value("\${security.password.encoder.keylength}") private val keylength: Int = 0

    override fun encode(cs: CharSequence): String {
        return try {
            val result =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(
                        PBEKeySpec(
                            cs.toString().toCharArray(),
                            secret.toByteArray(),
                            iteration,
                            keylength
                        )
                    )
                    .encoded
            Base64.getEncoder().encodeToString(result)
        } catch (ex: NoSuchAlgorithmException) {
            throw RuntimeException(ex)
        } catch (ex: InvalidKeySpecException) {
            throw RuntimeException(ex)
        }
    }

    override fun matches(cs: CharSequence, string: String): Boolean {
        return encode(cs) == string
    }
}
