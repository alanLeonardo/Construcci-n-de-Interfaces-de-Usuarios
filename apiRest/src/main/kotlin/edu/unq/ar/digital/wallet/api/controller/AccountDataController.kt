package edu.unq.ar.digital.wallet.api.controller

import edu.unq.ar.digital.wallet.api.handler.BadRequestHandler
import edu.unq.ar.digital.wallet.api.handler.NotFoundHandler
import edu.unq.ar.digital.wallet.model.adapter.AccountDataAdapter
import wallet.DigitalWallet
import io.javalin.http.Context
import wallet.BlockedAccountException

class AccountDataController(private val digitalWallet : DigitalWallet) {

    fun getData(ctx: Context) {
        val cvu = ctx.pathParam("cvu")
        try {
            validation(cvu)
            val account = digitalWallet.accountByCVU(cvu)
            if (account.isBlocked) {
                throw BlockedAccountException("Account with cvu ${account.cvu} is blocked and unable to perform operations.")
            }
            val saldo = account.balance
            val email = account.user.email
            val firstName = account.user.firstName
            ctx.json(AccountDataAdapter(cvu,saldo,email,firstName))
            ctx.status(200)
        } catch (e: BlockedAccountException) {
            ctx.status(400)
            ctx.json(BadRequestHandler(e.message!!))
        } catch (e: NoSuchElementException) {
            ctx.status(404)
            ctx.json(NotFoundHandler(e.message!!))
        } catch (e: IllegalStateException) {
            ctx.status(404)
            ctx.json(BadRequestHandler(e.message!!))
        }
    }

    private fun validation(cvu: String) {
        check(cvu.matches(Regex("^[0-9]{9}$"))) { "The CVU must be composed by numbers of 9 digits." }
    }
}
