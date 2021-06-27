package edu.unq.ar.digital.wallet.model.adapter

import wallet.Account
import wallet.User

// Objeto que parsea un User de DigitalWallet, a un BasicUserAdapter, que corta su bucle referencial.
object UserToFriendlyJSONFormat {
    fun parse(user: User): BasicUserAdapter {
        val account: Account = user.account!!
        return BasicUserAdapter(user.firstName, user.lastName, user.idCard, user.email, user.isAdmin,
            BasicAccountAdapter(account.cvu, account.balance, account.isBlocked,
                LoyaltiesListAdapter.parse(account.appliedLoyalties),
                TransactionsListAdapter.parse(account.transactions)))
    }

    fun parseWithPassword(user: User): CompleteUserAdapter {
        val account: Account = user.account!!
        return CompleteUserAdapter(user.firstName, user.lastName, user.idCard, user.email, user.password, user.isAdmin,
            BasicAccountAdapter(account.cvu, account.balance, account.isBlocked,
                LoyaltiesListAdapter.parse(account.appliedLoyalties),
                TransactionsListAdapter.parse(account.transactions)))
    }
}