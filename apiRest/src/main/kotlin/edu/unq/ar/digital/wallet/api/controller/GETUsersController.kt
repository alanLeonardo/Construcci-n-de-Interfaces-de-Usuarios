package edu.unq.ar.digital.wallet.api.controller

import edu.unq.ar.digital.wallet.model.adapter.*
import io.javalin.http.Context
import wallet.DigitalWallet
import wallet.User

class GETUsersController(private val digitalWallet : DigitalWallet){
    fun getUserList(ctx: Context) {
        ctx.status(200)
        ctx.json(digitalWallet.users.map {
            BasicUserAdapter(
                it.firstName,
                it.lastName,
                it.idCard,
                it.email,
                it.isAdmin,
                BasicAccountAdapter(
                    it.account!!.cvu,
                    it.account!!.balance,
                    it.account!!.isBlocked,
                    getListOfLoyalties(it),
                    getListOfTransactions(it)
                )
            )
        })
    }

    // Dado un User de DigitalWallet, retorna una Lista de sus Transacciones.
    private fun getListOfTransactions(user: User): List<BasicTransactionAdapter> {
        return user.account!!.transactions.map { transactional ->
            BasicTransactionAdapter(
                transactional.amount,
                DateTimeAdapter.getDate(transactional.dateTime),
                DateTimeAdapter.getHour(transactional.dateTime),
                transactional.description(),
                transactional.fullDescription(),
                transactional.isCashOut()
            )
        }
    }

    // Dado un User de DigitalWallet, retorna una lista de los Beneficios Aplicados a su cuenta.
    private fun getListOfLoyalties(user: User): List<LoyaltyGiftAdapter> {
        return user.account!!.appliedLoyalties.map { loyaltyGift ->
            LoyaltyGiftAdapter(
                loyaltyGift.name,
                loyaltyGift.strategy.javaClass.simpleName,
                loyaltyGift.strategy.appliedValue(),
                loyaltyGift.minNumberOfTransactions,
                loyaltyGift.minAmountPerTransaction,
                DateTimeAdapter.getDate(loyaltyGift.validFrom),
                DateTimeAdapter.getDate(loyaltyGift.validTo)
            )
        }
    }
}

