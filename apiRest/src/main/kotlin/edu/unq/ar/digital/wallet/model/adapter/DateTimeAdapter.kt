package edu.unq.ar.digital.wallet.model.adapter

import java.time.LocalDate
import java.time.LocalDateTime

object DateTimeAdapter {

    fun getDate(dateTime: LocalDateTime): String {
        return dateTime.year.toString()+"-"+dateTime.monthValue.toString()+"-"+dateTime.dayOfMonth.toString()
    }

    fun getDate(dateTime: LocalDate): String {
        return dateTime.year.toString()+"-"+dateTime.monthValue.toString()+"-"+dateTime.dayOfMonth.toString()
    }

    fun getHour(dateTime: LocalDateTime): String {
        return dateTime.hour.toString()+":"+dateTime.minute.toString()+":"+dateTime.second.toString()
    }
}