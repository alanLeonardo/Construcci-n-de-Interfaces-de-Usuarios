package edu.unq.ar.digital.wallet.api.handler

data class ConflictHandler(override val message: String): Handler(409, "CONFLICT", message)