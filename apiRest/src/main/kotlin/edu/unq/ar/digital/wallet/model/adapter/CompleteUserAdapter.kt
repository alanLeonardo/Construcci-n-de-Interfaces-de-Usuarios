package edu.unq.ar.digital.wallet.model.adapter

data class CompleteUserAdapter(
    val firstName: String,
    val lastName: String,
    val idCard: String,
    val email: String,
    val password: String,
    val admin: Boolean,
    val account: BasicAccountAdapter
)
