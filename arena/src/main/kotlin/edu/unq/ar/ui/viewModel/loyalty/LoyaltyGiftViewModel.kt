package edu.unq.ar.ui.viewModel.loyalty

import org.uqbar.commons.model.annotations.Dependencies
import org.uqbar.commons.model.annotations.Observable
import org.uqbar.commons.model.exceptions.UserException
import wallet.DiscountGiftStrategy
import wallet.FixedGiftStrategy
import wallet.LoyaltyGift
import wallet.LoyaltyGiftStrategy
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Observable
class LoyaltyGiftViewModel() {
    constructor(loyaltyGift: LoyaltyGift) : this() {
        nameLoyalty = loyaltyGift.name
        validFrom = loyaltyGift.validFrom
        validTo = loyaltyGift.validTo
        minAmountPerTransaction = loyaltyGift.minAmountPerTransaction.toInt()
        minNumberOfTransactions = loyaltyGift.minNumberOfTransactions
        strategy = this.obtainTypeOfStrategy(loyaltyGift.strategy)
    }

    fun obtainTypeOfStrategy(loyalty: LoyaltyGiftStrategy): String {
        return if (loyalty is FixedGiftStrategy) {
            "Regalo"
        } else {
            "Descuento"
        }
    }

    var nameLoyalty: String? = null
    var validFrom: LocalDate? = null
    var validTo: LocalDate? = null
    var strategy: String? = "Regalo"
    var amount: Int? = null
    var percentage: Int? = null
    var minNumberOfTransactions: Int? = null
    var minAmountPerTransaction: Int? = null
    var switchStrategy: MutableList<String> = mutableListOf("Regalo","Descuento")
    var isDiscountStrategy: Boolean? = this.getIsDiscountStrategy()
    var isGiftStrategy:Boolean? = this.getIsGiftStrategy()
    var checkValidFrom: String? = null

    @Dependencies("strategy")
    fun getIsDiscountStrategy(): Boolean? = this.strategy?.contains("Descuento")

    @Dependencies("strategy")
    fun getIsGiftStrategy(): Boolean? = this.strategy?.contains("Regalo")

    fun checkFields() {
        checkName()
        checkDates()
        checkStrategy()
        checkMinTransactions()
    }

    private fun checkName() {
        if (nameLoyalty == null || nameLoyalty == "") throw UserException("Debe indicar el \"Nombre\" del Beneficio.")
    }

    private fun checkDates() {
        try {
            checkValidFrom()
            checkValidTo()
            isValidFromTodayOrAfter()
            isValidFromBeforeValidTo()
        } catch (e: NullPointerException) {
            throw UserException("Los campos de fechas no pueden estar vacios.")
        }
    }

    private fun checkValidFrom() {
        if (validFrom == null) throw UserException("Ooops! La \"Fecha Desde\" que empieza el Beneficio " +
                "no es valida. Deberia revisar el patron, y asegurarse de que la fecha ingresada existe.")
    }

    private fun checkValidTo() {
        if (validTo == null) throw UserException("Ooops! La \"Fecha Hasta\" que termina el Beneficio " +
                " no es valida. Deberia revisar el patron y asegurarse de que la fecha ingresada existe.")
    }

    private fun isValidFromBeforeValidTo() {
        if (diffInDays(validFrom, validTo) < 0) {
            throw UserException("El campo \"Fecha Desde\" es anterior al campo \"Fecha Hasta\".")
        }
    }

    private fun isValidFromTodayOrAfter() {
        if (diffInDays(validFrom, LocalDate.now()) > 0) {
            print(validFrom)
            print(LocalDate.now())
            throw UserException("El campo \"Fecha Desde\" es anterior al dia de hoy." +
                    " Debe asignar una fecha como la de hoy o posterior, sino, puede afectar " +
                    "a la consistencia del sistema de transacciones.")
        }
    }

    private fun diffInDays(aDate: LocalDate?, otherDate: LocalDate?) = ChronoUnit.DAYS.between(aDate, otherDate).toInt()

    private fun checkStrategy() {
        checkGift()
        checkDiscount()
    }

    private fun checkGift() {
        if (strategy == "Regalo" && amount == null) throw UserException("Debe indicar el \"Importe del Regalo\".")
    }

    private fun checkDiscount() {
        if (strategy == "Descuento" && percentage == null) throw UserException("Debe indicar el \"Porcentaje de Descuento\".")
    }

    private fun checkMinTransactions() {
        checkMinNumberOfTransactions()
        checkMinAmountPerTransaction()
    }

    private fun checkMinAmountPerTransaction() {
        if (minAmountPerTransaction == null) throw UserException("Ooops! Parece que olvido indicar el \"Importe de cada Operacion\".")
    }

    private fun checkMinNumberOfTransactions() {
        if (minNumberOfTransactions == null) throw UserException("Ooops! Parece que olvido indicar la \"Cantidad de Operaciones\".")
    }

    fun createModel(): LoyaltyGift {
        return LoyaltyGift(this.nameLoyalty!!, strategyToModel(), minNumberOfTransactions!!, minAmountPerTransaction!!.toDouble(), validFrom!!, validTo!!)
    }

    private fun strategyToModel(): LoyaltyGiftStrategy {
        return if (getIsDiscountStrategy()!!) {
            DiscountGiftStrategy(this.percentage!!.toDouble())
        } else {
            FixedGiftStrategy(this.amount!!.toDouble())
        }
    }
}