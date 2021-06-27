package edu.unq.ar.digital.wallet.api.controller

import edu.unq.ar.digital.wallet.api.handler.BadRequestHandler
import edu.unq.ar.digital.wallet.api.handler.NotFoundHandler
import io.javalin.http.Context
import wallet.DigitalWallet

class DeleteController(private val digitalWallet : DigitalWallet) {

    fun deleteUser(ctx: Context) {
        val userCVU = ctx.pathParam("cvu")
        try {
            validation(userCVU)
            digitalWallet.deleteUser(digitalWallet.accountByCVU(userCVU).user)
            ctx.status(204)
        } catch (e: IllegalArgumentException) {
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

    private fun validation(userCVU: String) {
        check(userCVU.matches(Regex("^[0-9]{9}$"))) { "The CVU must be composed by numbers of 9 digits." }
    }
}
