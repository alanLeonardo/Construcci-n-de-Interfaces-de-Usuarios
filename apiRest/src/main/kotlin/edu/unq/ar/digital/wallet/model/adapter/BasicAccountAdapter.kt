package edu.unq.ar.digital.wallet.model.adapter

data class BasicAccountAdapter(
    val cvu: String,
    val balance: Double,
    val isBlocked: Boolean,
    val appliedLoyalties: List<LoyaltyGiftAdapter>,
    val transactions: List<BasicTransactionAdapter>
)