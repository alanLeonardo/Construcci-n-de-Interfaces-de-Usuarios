package edu.unq.ar.digital.wallet.model.adapter

import wallet.DiscountGiftStrategy
import wallet.FixedGiftStrategy
import wallet.LoyaltyGift
import wallet.LoyaltyGiftStrategy
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class StoreLoyaltyAdapter(val name: String,
                               val strategyName: String,
                               val strategyValue: Double,
                               val minNumberOfTransactions: Int,
                               val minAmountPerTransaction: Double,
                               val validFrom: String,
                               val validTo: String
) {
    fun toLoyaltyGift(): LoyaltyGift = LoyaltyGift(
        name,
        toStrategy(strategyName, strategyValue),
        minNumberOfTransactions,
        minAmountPerTransaction,
        LocalDate.parse(validFrom, DateTimeFormatter.ISO_DATE),
        LocalDate.parse(validTo, DateTimeFormatter.ISO_DATE)
    )

    private fun toStrategy(strategyName: String, strategyValue: Double): LoyaltyGiftStrategy {
        return if (strategyName == "Discount") {
            DiscountGiftStrategy(strategyValue)
        } else {
            FixedGiftStrategy(strategyValue)
        }
    }
}