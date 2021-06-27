package edu.unq.ar.digital.wallet.api.handler

data class NotFoundHandler(override val message: String): Handler(404, "NOT FOUND", message)