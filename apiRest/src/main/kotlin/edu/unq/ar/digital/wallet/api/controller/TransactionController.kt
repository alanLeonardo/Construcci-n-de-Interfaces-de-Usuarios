package edu.unq.ar.digital.wallet.api.controller

import edu.unq.ar.digital.wallet.api.handler.BadRequestHandler
import edu.unq.ar.digital.wallet.api.handler.NotFoundHandler
import edu.unq.ar.digital.wallet.model.adapter.BasicTransactionAdapter
import edu.unq.ar.digital.wallet.model.adapter.DateTimeAdapter
import io.javalin.http.Context
import wallet.Account
import wallet.DigitalWallet

class TransactionController(private val digitalWallet : DigitalWallet) {

    fun getTransactionsByCVU(ctx: Context) {
        try {
            check(ctx.pathParam("cvu").matches(Regex("^[0-9]{9}$"))) {"The CVU must be composed by numbers of 9 digits."}
            val account: Account = digitalWallet.accountByCVU(ctx.pathParam("cvu"))
            ctx.json(account.transactions.map {
                BasicTransactionAdapter(
                    it.amount,
                    DateTimeAdapter.getDate(it.dateTime),
                    DateTimeAdapter.getHour(it.dateTime),
                    it.description(),
                    it.fullDescription(),
                    it.isCashOut()) })
        } catch (e: IllegalStateException) {
            ctx.status(400)
            ctx.json(BadRequestHandler(e.message!!))
        } catch (e: NoSuchElementException) {
            ctx.status(404)
            ctx.json(NotFoundHandler(e.message!!))
        }
    }
}
