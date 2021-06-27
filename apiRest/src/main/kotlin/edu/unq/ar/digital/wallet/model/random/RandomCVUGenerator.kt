package edu.unq.ar.digital.wallet.model.random

import kotlin.random.Random

class RandomCVUGenerator {
    private var random = Random(5989)

    fun generate(): String {
        return "${random.nextInt(0, 1000000000)}".padStart(9, '0')
    }

}