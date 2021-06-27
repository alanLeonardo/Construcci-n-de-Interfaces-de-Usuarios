package edu.unq.ar.digital.wallet.api.controller

import edu.unq.ar.digital.wallet.api.handler.BadRequestHandler
import edu.unq.ar.digital.wallet.api.handler.Handler
import edu.unq.ar.digital.wallet.model.adapter.*
import io.javalin.core.validation.Validator
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import wallet.*

class LoginUserController(private val digitalWallet : DigitalWallet) {

    fun loginUser(ctx: Context) {
        try{
            val loginUserValidator = ctx.bodyValidator(LoginUserAdapter::class.java)
            validation(loginUserValidator)
            val loginUserAdapter = loginUserValidator.get()
            val user: User = digitalWallet.login(loginUserAdapter.email , loginUserAdapter.password)
            ctx.status(200)
            ctx.json(UserToFriendlyJSONFormat.parse(user))
        } catch (e : LoginException) {
            ctx.status(400)
            ctx.json(BadRequestHandler(e.message!!))
        } catch (e: IllegalArgumentException) {
            ctx.status(400)
            ctx.json(BadRequestHandler(e.message!!))
        } catch (e: BadRequestResponse) {
            ctx.status(400)
            ctx.json(BadRequestHandler(e.message!!))
        } catch (e: Exception) {
            ctx.status(500)
            ctx.json(Handler(500, "INTERNAL SERVER ERROR", e.toString()))
        }
    }

    private fun validation(loginUserValidator: Validator<LoginUserAdapter>) {
        validateFields(loginUserValidator)
        validateAccount(loginUserValidator)
    }

    private fun validateAccount(loginUserValidator: Validator<LoginUserAdapter>) {
        validateAccountExist(loginUserValidator)
        validateAccountNotBlocked(loginUserValidator)
    }

    private fun validateAccountNotBlocked(loginUserValidator: Validator<LoginUserAdapter>) {
        val loginUserAdapter = loginUserValidator.get()
        val user = digitalWallet.users.find { user -> user.email == loginUserAdapter.email }!!
        loginUserValidator.check({ !user.account!!.isBlocked }, "The account is blocked.").get()
    }

    private fun validateAccountExist(loginUserValidator: Validator<LoginUserAdapter>) {
        val emails = digitalWallet.users.map { user -> user.email }
        loginUserValidator.check(
            { emails.contains(it.email) },
            "The account doesn't exist. Check your email and password."
        ).get()
    }

    private fun validateFields(loginUserValidator: Validator<LoginUserAdapter>) {
        loginUserValidator.check({ it.email.isNotBlank() }, "Email field can't be blank.").get()
        loginUserValidator.check({ it.password.isNotBlank() }, "Password field can't be blank.").get()
        loginUserValidator.check(
            { it.email.matches("^[\\w.-]{1,20}@\\w{3,7}\\.[a-zA-Z]{2,3}$".toRegex()) },
            "Email field only use an email format (name_surname@email.com)."
        ).get()
    }
}