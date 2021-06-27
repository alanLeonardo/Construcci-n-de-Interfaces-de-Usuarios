package edu.unq.ar.digital.wallet.api.handler

data class BadRequestHandler(override val message: String): Handler(400, "BAD REQUEST", message)