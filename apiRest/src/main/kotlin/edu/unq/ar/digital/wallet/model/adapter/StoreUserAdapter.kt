package edu.unq.ar.digital.wallet.model.adapter

import wallet.User

data class StoreUserAdapter(val firstName: String,
                            val lastName: String,
                            val email: String,
                            val idCard: String,
                            val password: String) {

    fun toUser(): User = User(idCard, firstName, lastName, email, password, false)
}