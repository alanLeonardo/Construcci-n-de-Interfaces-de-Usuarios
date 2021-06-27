package edu.unq.ar.digital.wallet.model.adapter

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CashInUserAdapter(val fromCVU: String,
                             val amount: Double?,
                             val cardNumber: String,
                             val fullName: String,
                             val endDate: String,
                             val securityCode: String,
                             val isCreditCard : String
                             ) {
    fun parseEndDate(): LocalDate {
        return LocalDate.parse("01-"+endDate.take(2)+"-"+endDate.takeLast(4), DateTimeFormatter.ofPattern("dd-MM-yyyy"))!!
    }
}