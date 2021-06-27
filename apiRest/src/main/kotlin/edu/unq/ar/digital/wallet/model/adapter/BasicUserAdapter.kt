package edu.unq.ar.digital.wallet.model.adapter

data class BasicUserAdapter(
    val firstName: String,
    val lastName: String,
    val idCard: String,
    val email: String,
    val isAdmin: Boolean,
    val account: BasicAccountAdapter
)