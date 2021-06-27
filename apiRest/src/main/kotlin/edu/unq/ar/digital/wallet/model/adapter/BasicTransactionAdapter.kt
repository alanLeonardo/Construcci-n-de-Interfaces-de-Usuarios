package edu.unq.ar.digital.wallet.model.adapter

data class BasicTransactionAdapter(
    val amount: Double,
    val date: String,
    val hour: String,
    val description: String,
    val fullDescription: String,
    val cashOut: Boolean)