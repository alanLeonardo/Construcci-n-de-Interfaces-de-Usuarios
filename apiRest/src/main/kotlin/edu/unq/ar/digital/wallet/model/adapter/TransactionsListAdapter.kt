package edu.unq.ar.digital.wallet.model.adapter

import wallet.Transactional

// Objecto que parsea Listas de Transacciones de DigitalWallet a una lista
// que es mas favorable para mostrar via JSON.
object TransactionsListAdapter {
    fun parse(transactions: MutableList<Transactional>): List<BasicTransactionAdapter> {
        return transactions.map { transactional ->
            BasicTransactionAdapter(
                transactional.amount,
                DateTimeAdapter.getDate(transactional.dateTime),
                DateTimeAdapter.getHour(transactional.dateTime),
                transactional.description(),
                transactional.fullDescription(),
                transactional.isCashOut()
            )
        }
    }
}