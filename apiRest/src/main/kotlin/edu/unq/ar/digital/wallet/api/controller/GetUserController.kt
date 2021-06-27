package edu.unq.ar.digital.wallet.api.controller

import edu.unq.ar.digital.wallet.api.handler.BadRequestHandler
import edu.unq.ar.digital.wallet.api.handler.Handler
import edu.unq.ar.digital.wallet.api.handler.NotFoundHandler
import edu.unq.ar.digital.wallet.model.adapter.UserToFriendlyJSONFormat
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import wallet.DigitalWallet

class GetUserController(val digitalWallet: DigitalWallet) {
    fun getUserByCVU(ctx:Context) {
        try {
            val cvu: String = ctx.pathParam("cvu")
            validation(cvu)
            val user = digitalWallet.accountByCVU(cvu).user
            ctx.status(200)
            ctx.json(UserToFriendlyJSONFormat.parseWithPassword(user))
        } catch (e:IllegalArgumentException) {
            ctx.status(400).json(BadRequestHandler(e.message!!))
        } catch (e:BadRequestResponse) {
            ctx.status(400).json(BadRequestHandler(e.message!!))
        } catch (e: NotFoundResponse) {
            ctx.status(404).json(NotFoundHandler(e.message!!))
        } catch (e:Exception) {
            ctx.status(500).json(Handler(500, e.javaClass.simpleName, e.message!!))
        }
    }

    private fun validation(cvu: String) {
        check(cvu.matches(Regex("^[0-9]{9}$"))) { "The CVU must be composed by numbers of 9 digits." }
    }
}