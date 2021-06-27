package edu.unq.ar.digital.wallet.model.adapter

data class LoyaltyGiftAdapter(
    val name: String,
    val strategyName: String,
    val strategyValue: Double,
    val minNumberOfTransactions: Int,
    val minAmountPerTransaction: Double,
    val validFrom: String,
    val validTo: String
)