package edu.unq.ar.ui.viewModel.utilities

import edu.unq.ar.ui.viewModel.transaction.Transaction
import wallet.*
import java.time.LocalDate
import java.time.LocalDateTime

object DigitalWalletFactory {
    private val digitalWalletSystem = DigitalWallet()
//    private val digitalWalletSystem: DigitalWallet = DigitalWalletData.build()
    init {
    initUsers()
    initLoyalties()
    }

    private fun initLoyalties() {
        val discountStrategy = DiscountGiftStrategy(20.0)
        val fixedStrategy =  FixedGiftStrategy(200.00)
        val loyalty1 = LoyaltyGift("Descuento del 20%", discountStrategy, 4, 1200.0, LocalDate.parse("2019-09-20"), LocalDate.parse("2019-10-20"))
        val loyalty2 = LoyaltyGift("Regalo de $200.00", fixedStrategy, 2, 600.0, LocalDate.parse("2019-09-20"), LocalDate.parse("2019-10-20"))
        digitalWalletSystem.addLoyalty(loyalty1)
        digitalWalletSystem.addLoyalty(loyalty2)
    }

    private fun initUsers() {
        val user1 = User("30556993", "Jon", "Snow", "jon.snow@gmail.com", "jsnow", true)
        val cvu: String = DigitalWallet.generateNewCVU()
        val account1 = Account(user1, cvu)
        account1.isBlocked = false
        val transaction1 =
            Transaction(3000.0, LocalDateTime.now(), true, "MercadoLibre", "SSD")
        val transaction2 =
            Transaction(1000.0, LocalDateTime.now(), true, "ICBC", "Extracción")
        val transaction3 =
            Transaction(
                5000.0,
                LocalDateTime.now(),
                false,
                "Transferencia",
                "Varios"
            )
        account1.addTransaction(transaction1)
        account1.addTransaction(transaction2)
        account1.addTransaction(transaction3)
        val user2 = User("35993240", "Daenerys", "Targaryen", "dany_targaryen@outlook.com", "******", true)
        val cvu2: String = DigitalWallet.generateNewCVU()
        val account2 = Account(user2, cvu2)
        account2.isBlocked = true
        val user3 = User("24955828", "Cercei", "Lannister", "cercei.lannister@gmail.com", "******", false)
        val cvu3: String = DigitalWallet.generateNewCVU()
        val account3 = Account(user3, cvu3)

        digitalWalletSystem.register(user1)
        digitalWalletSystem.register(user2)
        digitalWalletSystem.register(user3)
        digitalWalletSystem.assignAccount(user1, account1)
        digitalWalletSystem.assignAccount(user2, account2)
        digitalWalletSystem.assignAccount(user3, account3)
    }

    fun makeDigitalWalletSystem() = digitalWalletSystem
}
