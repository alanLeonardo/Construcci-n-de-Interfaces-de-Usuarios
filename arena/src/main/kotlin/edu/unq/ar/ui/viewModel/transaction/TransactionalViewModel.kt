package edu.unq.ar.ui.viewModel.transaction

import org.uqbar.commons.model.annotations.Observable
import wallet.Transactional
import java.time.LocalDateTime

@Observable
class TransactionalViewModel(var transactional: Transactional) {

    var amount : Double? = transactional.amount
    var dateTime : LocalDateTime? = transactional.dateTime
    var description = transactional.description()
    var fullDescription = transactional.fullDescription()
    var esCashOut = transactional.isCashOut()

    fun model() = transactional
}
