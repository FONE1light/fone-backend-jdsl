package com.fone.common

import kotlin.random.Random

object TestGenerator {
    private val phoneSequence = sequence {
        val randomPhoneNumberFunc =
            { "010-" + "%04d".format(Random.nextInt(0, 10000)) + "-%04d".format(Random.nextInt(0, 10000)) }
        val alreadyExists = mutableSetOf<String>()
        while (true) {
            var randomPhoneNumber = randomPhoneNumberFunc()
            if (alreadyExists.contains(randomPhoneNumber)) {
                while (alreadyExists.contains(randomPhoneNumber)) {
                    randomPhoneNumber = randomPhoneNumberFunc()
                }
            }
            alreadyExists.add(randomPhoneNumber)
            yield(randomPhoneNumber)
        }
    }.iterator()

    fun getRandomPhoneNumber(): String = phoneSequence.next()
}
