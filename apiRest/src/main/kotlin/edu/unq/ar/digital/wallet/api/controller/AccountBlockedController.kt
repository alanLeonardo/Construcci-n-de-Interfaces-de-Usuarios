package edu.unq.ar.digital.wallet.api.controller

import edu.unq.ar.digital.wallet.api.handler.BadRequestHandler
import edu.unq.ar.digital.wallet.model.adapter.BasicAccountAdapter
import edu.unq.ar.digital.wallet.model.adapter.LoyaltiesListAdapter
import edu.unq.ar.digital.wallet.model.adapter.TransactionsListAdapter
import io.javalin.http.Context
import wallet.DigitalWallet

class AccountBlockedController(val digitalWallet: DigitalWallet) {
    fun putAccountBlocked(ctx: Context) {
        val cvu = ctx.pathParam("cvu")
        try {
            val account = digitalWallet.accountByCVU(cvu)
            digitalWallet.blockAccount(account)
            ctx.status(201)
            ctx.json(BasicAccountAdapter(cvu, account.balance, account.isBlocked,
                LoyaltiesListAdapter.parse(account.appliedLoyalties), TransactionsListAdapter.parse(account.transactions) ))
        } catch (e: NoSuchElementException) {
            ctx.status(400)
            ctx.json(BadRequestHandler(e.message!!))
        }
    }
}