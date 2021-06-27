package edu.unq.ar.digital.wallet.api.controller

import edu.unq.ar.digital.wallet.api.handler.BadRequestHandler
import edu.unq.ar.digital.wallet.model.adapter.LoyaltyGiftAdapter
import io.javalin.http.Context
import wallet.DigitalWallet
import edu.unq.ar.digital.wallet.model.adapter.StoreLoyaltyAdapter
import io.javalin.core.validation.Validator
import io.javalin.http.BadRequestResponse
import java.time.LocalDate

class LoyaltyController(private val digitalWallet : DigitalWallet) {
    // Return all the Loyalties from the context.
    fun getLoyalty(ctx: Context) {
        ctx.status(200)
        ctx.json(digitalWallet.loyaltyGifts.map{
            LoyaltyGiftAdapter(
                it.name,
                it.strategy.javaClass.simpleName,
                it.strategy.appliedValue(),
                it.minNumberOfTransactions,
                it.minAmountPerTransaction,
                it.validFrom.toString(),
                it.validTo.toString()

            )
        })
    }

    // POST a Loyalty to the context.
    fun postLoyalty(ctx: Context) {

        try {
            val loyaltyValidator = ctx.bodyValidator(StoreLoyaltyAdapter::class.java)
            validationFields(loyaltyValidator)
            val storeLoyaltyAdapter = loyaltyValidator.get()
            digitalWallet.addLoyalty(storeLoyaltyAdapter.toLoyaltyGift())
            ctx.status(201)
            ctx.json(storeLoyaltyAdapter)
        } catch(e: IllegalArgumentException) {
            ctx.status(400)
            ctx.json(BadRequestHandler(e.message!!))
        } catch(e: BadRequestResponse) {
            ctx.status(400)
            if (e.message!!.contains("Couldn't deserialize body to StoreLoyaltyAdapter")) {
                ctx.json(BadRequestHandler("Must input all fields from Loyalty: name, strategyName, strategyValue, " +
                        "minNumberOfTransactions, minAmountPerTransaction, validFrom and validTo."))
            } else {
                ctx.json(BadRequestHandler(e.message!!))
            }
        }
    }

    // Validate if the Loyalty fields aren't blank or with wrong format.
    private fun validationFields(loyaltyValidator: Validator<StoreLoyaltyAdapter>) {
        validationFieldsNotBlank(loyaltyValidator)
        validationFieldsFormat(loyaltyValidator)
    }

    // Validate if the Loyalty fields aren't with wrong format.
    private fun validationFieldsFormat(loyaltyValidator: Validator<StoreLoyaltyAdapter>) {
        val validFrom = LocalDate.parse(loyaltyValidator.get().validFrom)
        val validTo = LocalDate.parse(loyaltyValidator.get().validTo)
        loyaltyValidator.check({ it.minAmountPerTransaction > 0 },
            "Min amount per Transaction must be bigger than $0.00.").get()
        loyaltyValidator.check({ it.minNumberOfTransactions > 0},
            "Min number of transactions must be bigger than zero.").get()
        loyaltyValidator.check({ validFrom.isBefore(validTo)},
            "ValidFrom must be before than ValidTo.").get()
        loyaltyValidator.check({ validTo.isAfter(validFrom)},
            "ValidTo must be after than ValidFrom").get()
        loyaltyValidator.check({it.strategyName.toLowerCase() == "fixed"||it.strategyName.toLowerCase()=="discount"},
            "StrategyName must be fixed or discount").get()
        loyaltyValidator.check({ it.strategyValue > 0}, "Strategy Value must be bigger than zero.").get()
        loyaltyValidator.check({ (it.strategyValue <= 100 && it.strategyName.toLowerCase() == "discount")
                || (it.strategyValue > 0 && it.strategyName.toLowerCase() == "fixed")},
            "An discount must be minor or equal than 100%.").get()
    }

    // Validate if the Loyalty fields aren't blank.
    private fun validationFieldsNotBlank(loyaltyValidator: Validator<StoreLoyaltyAdapter>) {
        loyaltyValidator.check({ it.name.isNotBlank() }, "Name field can`t be blank.").get()
        loyaltyValidator.check({ it.strategyName.isNotBlank() }, "StrategyName field can't be blank.").get()
        loyaltyValidator.check({ it.validFrom.isNotBlank()}, "ValidFrom field can't be blank.").get()
        loyaltyValidator.check({ it.validTo.isNotBlank()}, "ValidTo field can't be blank.").get()
    }

}