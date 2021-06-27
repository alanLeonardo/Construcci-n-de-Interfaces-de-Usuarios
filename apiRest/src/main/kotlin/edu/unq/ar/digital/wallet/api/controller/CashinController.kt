package edu.unq.ar.digital.wallet.api.controller

import edu.unq.ar.digital.wallet.api.handler.Handler
import edu.unq.ar.digital.wallet.model.adapter.BasicCashInAdapter
import edu.unq.ar.digital.wallet.model.adapter.CashInUserAdapter
import io.javalin.core.validation.Validator
import io.javalin.http.Context
import wallet.Account
import wallet.CreditCard
import wallet.DebitCard
import wallet.DigitalWallet
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CashinController(private val digitalWallet : DigitalWallet) {

    fun cashIn(ctx: Context) {
        val cashinValidator = ctx.bodyValidator<CashInUserAdapter>()
        try {
            validationFields(cashinValidator)
            val cashinAdapter = cashinValidator.get()
            if(cashinAdapter.isCreditCard.toLowerCase() == ("credit")) {
                digitalWallet.transferMoneyFromCard(cashinAdapter.fromCVU,CreditCard(cashinAdapter.cardNumber,cashinAdapter.fullName,
                    cashinAdapter.parseEndDate(),cashinAdapter.securityCode),cashinAdapter.amount!!)
            } else {
                digitalWallet.transferMoneyFromCard(cashinAdapter.fromCVU,DebitCard(cashinAdapter.cardNumber,cashinAdapter.fullName,
                    cashinAdapter.parseEndDate(),cashinAdapter.securityCode),cashinAdapter.amount!!)
            }
            ctx.status(201)
            ctx.json(BasicCashInAdapter(cashinAdapter.fromCVU, cashinAdapter.amount))
        } catch (e: Exception) {
            ctx.status(400)
            ctx.json(Handler(400, "Bad request", e.message!!))
        }
    }

    private fun validationFields(transferValidator: Validator<CashInUserAdapter>) {
        validationFieldsNotBlank(transferValidator)
        validationFieldsFormat(transferValidator)
        validationAccountUnblocked(transferValidator)
    }

    private fun validationFieldsFormat(transferValidator: Validator<CashInUserAdapter>) {
        transferValidator.check({ it.fromCVU.matches(Regex("^[0-9]{9}$")) }, "FromCVU field only use numeric characters of 9 digits.").get()
        transferValidator.check({ it.cardNumber.matches(Regex("^[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}$")) }, "CardNumber field only accepts a card number format (example:1234 1234 1234 1234).").get()
        transferValidator.check({ it.fullName.matches(Regex("^([a-zA-Z0-9]+|[a-zA-Z0-9]+\\s{1}[a-zA-Z0-9]{1,}|[a-zA-Z0-9]+\\s{1}[a-zA-Z0-9]{3,}\\s{1}[a-zA-Z0-9]{1,})\$")) }, "FullName field only accept letters and spaces.").get()
        transferValidator.check({ it.endDate.matches(Regex("[0-9]{2}/[0-9]{4}$")) }, "EndDate field only use numeric characters with format MM/yyyy.").get()
        transferValidator.check({checkEndDate(it.endDate)},"EndDate Card is deprecated").get()
        transferValidator.check({ it.securityCode.matches(Regex("^[0-9]{3}$")) }, "SecurityCode field only use numeric characters of 3 digits.").get()
        transferValidator.check({ it.amount!! > 0.00 }, "Amount can't be zero or negative.").get()
        transferValidator.check({ it.isCreditCard.toLowerCase() == "credit" || it.isCreditCard.toLowerCase() == "debit" }, "IsCreditCard field only accept credit or debit.").get()
    }

    private fun validationFieldsNotBlank(transferValidator: Validator<CashInUserAdapter>) {
        transferValidator.check({ it.fromCVU.isNotBlank() }, "CVU field can`t be blank.").get()
        transferValidator.check({ it.cardNumber.isNotBlank() }, "CardNumber field can`t be blank.").get()
        transferValidator.check({ it.fullName.isNotBlank() }, "FullName field can`t be blank.").get()
        transferValidator.check({ it.endDate.isNotBlank() }, "EndDate field can`t be blank.").get()
        transferValidator.check({ it.securityCode.isNotBlank() }, "SecurityCode field can`t be blank.").get()
        transferValidator.check({ it.isCreditCard.isNotBlank() }, "IsCreditCard field can`t be blank.").get()
    }

    private fun checkEndDate(endDate: String): Boolean {
        val today = LocalDate.now()
        val parsedEndDate = LocalDate.parse(
            "01-"+endDate.take(2)+
                "-"+endDate.takeLast(4)
            , DateTimeFormatter.ofPattern("dd-MM-yyyy"))!!
        return parsedEndDate.isAfter(today)
    }

    // Valida que la cuenta no este bloqueada.
    private fun validationAccountUnblocked(validator: Validator<CashInUserAdapter>) {
        val fromAccount: Account = digitalWallet.accountByCVU(validator.get().fromCVU)
        validator.check({!fromAccount.isBlocked}, "CVU Origin (from CVU) is blocked.")
    }
}