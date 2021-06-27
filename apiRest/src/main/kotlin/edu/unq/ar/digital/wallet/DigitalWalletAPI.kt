package edu.unq.ar.digital.wallet

import data.DigitalWalletData
import edu.unq.ar.digital.wallet.api.controller.*
import edu.unq.ar.digital.wallet.api.handler.NotFoundHandler
import edu.unq.ar.digital.wallet.model.exceptions.NotFound
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.util.RouteOverviewPlugin
import wallet.DigitalWallet

fun main() {
    val digitalWallet : DigitalWallet = DigitalWalletData.build()
    DigitalWalletAPI(7000, digitalWallet).init()
}

class DigitalWalletAPI(private val port: Int, private val digitalWallet: DigitalWallet) {
    fun init(): Javalin {
        val app = Javalin.create {
            it.defaultContentType = "application/json"
            it.registerPlugin(RouteOverviewPlugin("/routes"))
            it.enableCorsForAllOrigins()
        }.start(port)

        val digitalWalletController = DigitalWalletController(digitalWallet)

        app.routes {
            path("login") { post(digitalWalletController.loginUserController::loginUser) }              // POST login/

            path("users") {
                get(digitalWalletController.getUsersController::getUserList)              // GET users/
                path(":cvu") {get(digitalWalletController.getUserController::getUserByCVU)} // GET user/:cvu
                post(digitalWalletController.registerController::storeUser)               // POST users/
                path(":cvu") { delete(digitalWalletController.deleteController::deleteUser) }  // DELETE users/:cvu
            }

            path("transactions/:cvu") { get(digitalWalletController.transactionController::getTransactionsByCVU) } // GET transactions/:cvu

            path("account") {
                path("blocked/:cvu") { put(digitalWalletController.accountBlockedController::putAccountBlocked) }
                path("unblocked/:cvu") { put(digitalWalletController.accountUnblockedController::putAccountUnblocked) }
                path(":cvu") { get(digitalWalletController.amountController::getAmount) }                // GET account/:cvu
                path("data/:cvu") { get(digitalWalletController.accountDataController::getData) } // GET account/data/:cvu
                path("/user/:cvu") { patch(digitalWalletController.accountUserController::patchDataUser) } // PATCH account/user/:cvu
            }

            path("cashin") { post(digitalWalletController.cashinController::cashIn) }                   // POST /cashin

            path("transfer") { post(digitalWalletController.transferController::transfer) }                 // POST /transfer

            path("loyalty") { get(digitalWalletController.loyaltyController::getLoyalty) } // GET loyalty/
            path("loyalty") { post(digitalWalletController.loyaltyController::postLoyalty) } // POST loyalty/
        }

        app.exception(NotFound::class.java) { e, ctx ->
            ctx.status(404)
            ctx.json(NotFoundHandler(e.message!!))
        }
        return app
    }
}
