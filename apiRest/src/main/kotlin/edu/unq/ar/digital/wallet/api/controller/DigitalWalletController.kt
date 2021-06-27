package edu.unq.ar.digital.wallet.api.controller

import wallet.*

class DigitalWalletController(digitalWallet: DigitalWallet) {

    val loginUserController = LoginUserController(digitalWallet)
    val getUsersController = GETUsersController(digitalWallet)
    val registerController = RegisterController(digitalWallet)
    val deleteController = DeleteController(digitalWallet)
    val transactionController = TransactionController(digitalWallet)
    val amountController = AmountController(digitalWallet)
    val cashinController = CashinController(digitalWallet)
    val transferController = TransferController(digitalWallet)
    val accountBlockedController = AccountBlockedController(digitalWallet)
    val accountUnblockedController = AccountUnblockedController(digitalWallet)
    val accountDataController = AccountDataController(digitalWallet)
    val accountUserController = AccountUserController(digitalWallet)
    val loyaltyController = LoyaltyController(digitalWallet)
    val getUserController = GetUserController(digitalWallet)
}

