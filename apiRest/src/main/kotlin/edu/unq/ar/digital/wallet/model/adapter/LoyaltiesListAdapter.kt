package edu.unq.ar.digital.wallet.model.adapter

import wallet.LoyaltyGift

// Objecto que parsea Listas de Beneficios de DigitalWallet a una lista
// que es mas favorable para mostrar via JSON.
object LoyaltiesListAdapter {
    fun parse(appliedLoyalties: MutableList<LoyaltyGift>): List<LoyaltyGiftAdapter> {
        return appliedLoyalties.map {
            LoyaltyGiftAdapter(
                it.name,
                it.strategy.javaClass.simpleName,
                it.strategy.appliedValue(),
                it.minNumberOfTransactions,
                it.minAmountPerTransaction,
                DateTimeAdapter.getDate(it.validFrom),
                DateTimeAdapter.getDate(it.validTo)
            )
        }
    }
}
