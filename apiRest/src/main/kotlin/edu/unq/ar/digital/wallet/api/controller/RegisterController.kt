package edu.unq.ar.digital.wallet.api.controller

import edu.unq.ar.digital.wallet.api.handler.ConflictHandler
import edu.unq.ar.digital.wallet.api.handler.Handler
import edu.unq.ar.digital.wallet.model.adapter.StoreUserAdapter
import edu.unq.ar.digital.wallet.model.adapter.UserToFriendlyJSONFormat
import edu.unq.ar.digital.wallet.model.random.RandomCVUGenerator
import io.javalin.http.Context
import wallet.Account
import wallet.DigitalWallet
import wallet.InitialGift
import wallet.User
import java.time.LocalDateTime

class RegisterController(private val digitalWallet : DigitalWallet) {
    private val random = RandomCVUGenerator()

    fun storeUser(ctx: Context) {
        val storeUserValidator = ctx.bodyValidator(StoreUserAdapter::class.java)
        try {
            storeUserValidator.check({!it.firstName.isBlank()}, "Firstname field can`t be blank.").get()
            storeUserValidator.check({!it.lastName.isBlank()}, "Lastname field can`t be blank.").get()
            storeUserValidator.check({!it.password.isBlank()}, "Password field can`t be blank.").get()
            storeUserValidator.check({!it.idCard.isBlank()}, "IDCard field can`t be blank.").get()
            storeUserValidator.check({!it.email.isBlank()}, "Email field can`t be blank.").get()
            storeUserValidator.check({it.idCard.toIntOrNull() != null}, "IDCard field only accept numbers.").get()
            storeUserValidator.check({it.lastName.chars().allMatch(Character::isLetter)}, "Lastname field only accept letters.").get()
            storeUserValidator.check({it.firstName.chars().allMatch(Character::isLetter)}, "Firstname field only accept letters.").get()
            storeUserValidator.check({it.password.matches(Regex("[A-Za-z0-9]*"))}, "Password field only use alphanumeric characters.").get()
            storeUserValidator.check({it.email.matches("^[\\w.-]{1,20}@\\w{3,7}\\.[a-zA-Z]{2,3}$".toRegex())}, "Email field only use an email format (name_surname@email.com).").get()

            val user: User = storeUserValidator.get().toUser()
            digitalWallet.register(user)
            digitalWallet.assignAccount(user, Account(user, random.generate()))
            addInitialGiftTo(user)
            ctx.status(201)
            ctx.json(UserToFriendlyJSONFormat.parse(user))
        } catch (e: IllegalArgumentException) {
            ctx.status(409)
            ctx.json(ConflictHandler(e.message!!))
        } catch (e: Exception) {
            ctx.status(400)
            ctx.json(Handler(400, "Bad request", e.message!!))
        }
    }

    private fun addInitialGiftTo(user: User) {
        val account: Account = digitalWallet.users.find { it.idCard == user.idCard }!!.account!!
        digitalWallet.addGift(InitialGift(account, 200.00, LocalDateTime.now()))
    }
}
