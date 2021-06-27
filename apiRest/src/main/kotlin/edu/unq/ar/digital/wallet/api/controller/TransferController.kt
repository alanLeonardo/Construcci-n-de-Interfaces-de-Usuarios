package edu.unq.ar.digital.wallet.api.controller

import edu.unq.ar.digital.wallet.api.handler.BadRequestHandler
import edu.unq.ar.digital.wallet.api.handler.NotFoundHandler
import edu.unq.ar.digital.wallet.model.adapter.TransferAdapter
import io.javalin.core.validation.Validator
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import wallet.Account
import wallet.BlockedAccountException
import wallet.DigitalWallet
import wallet.NoMoneyException

class TransferController(private val digitalWallet : DigitalWallet) {

    fun transfer(ctx: Context) {
        val transferValidator = ctx.bodyValidator<TransferAdapter>()
        try {
            validationFields(transferValidator)
            val transferAdapter = transferValidator.get()
            digitalWallet.transfer(transferAdapter.fromCVU, transferAdapter.toCVU, -transferAdapter.amount)
            ctx.status(201)
            ctx.json(transferAdapter)
        }catch (e: BlockedAccountException) {
            ctx.status(400)
            ctx.json(BadRequestHandler(e.message!!))
        }catch (e: NoMoneyException) {
            ctx.status(400)
            ctx.json(BadRequestHandler(e.message!!))
        }catch (e: NoSuchElementException) {
            ctx.status(404)
            ctx.json(NotFoundHandler(e.message!!))
        }catch (e: BadRequestResponse) {
            ctx.status(400)
            ctx.json(BadRequestHandler(e.message!!))
        }
    }

    private fun validationFields(validator: Validator<TransferAdapter>) {
        validationFieldsNotBlank(validator)
        validationFieldsFormat(validator)
        validationAccountsUnblocked(validator)
    }

    private fun validationFieldsNotBlank(validator: Validator<TransferAdapter>) {
        validator.check({it.fromCVU.isNotBlank()}, "fromCVU field can't be blank.").get()
        validator.check({it.toCVU.isNotBlank()}, "toCVU field can't be blank.").get()
    }

    private fun validationFieldsFormat(validator: Validator<TransferAdapter>) {
        //validator.check({it.amount > 0.00}, "Amount can't be zero or negative.").get()
        validator.check({ it.fromCVU.matches(Regex("^[0-9]{9}$")) }, "FromCVU field only use numeric characters of 9 digits.").get()
        validator.check({ it.toCVU.matches(Regex("^[0-9]{9}$")) }, "toCVU field only use numeric characters of 9 digits.").get()
    }

    // Valida que las cuentas no esten bloqueadas
    private fun validationAccountsUnblocked(validator: Validator<TransferAdapter>) {
        val fromAccount: Account = digitalWallet.accountByCVU(validator.get().fromCVU)
        validator.check({!fromAccount.isBlocked}, "CVU Origin (from CVU) is blocked.")
        val toAccount: Account = digitalWallet.accountByCVU(validator.get().toCVU)
        validator.check({!toAccount.isBlocked}, "CVU Destiny (to CVU) is blocked.")
    }
}
