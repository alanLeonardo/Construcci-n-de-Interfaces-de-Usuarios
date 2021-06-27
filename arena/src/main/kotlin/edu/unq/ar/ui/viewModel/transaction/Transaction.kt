package edu.unq.ar.ui.viewModel.transaction

import wallet.Transactional
import java.time.LocalDateTime

class Transaction(var monto: Double,
                  var fecha: LocalDateTime,
                  var esCashOut: Boolean,
                  var description: String,
                  var fullDescription: String) : Transactional {

    override val amount: Double
        get() = monto
    override val dateTime: LocalDateTime
        get() = fecha

    override fun description(): String {
        return description
    }

    override fun fullDescription(): String {
        return fullDescription
    }

    override fun isCashOut(): Boolean {
        return esCashOut
    }
}
