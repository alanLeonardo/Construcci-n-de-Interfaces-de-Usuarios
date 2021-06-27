package edu.unq.ar.digital.wallet.api.controller

import edu.unq.ar.digital.wallet.api.handler.BadRequestHandler
import edu.unq.ar.digital.wallet.api.handler.Handler
import edu.unq.ar.digital.wallet.model.adapter.*
import io.javalin.core.validation.Validator
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import wallet.Account
import wallet.DigitalWallet
import wallet.User

class AccountUserController(val digitalWallet: DigitalWallet) {
    // Edita el Nombre, Apellido y Email de un User.
    // Request Body: {"firstName": "unNombre", "lastName": "unApellido", "email": "unCorreo" }
    fun patchDataUser(ctx: Context) {
        try {
            val oldAccount = digitalWallet.accountByCVU(ctx.pathParam("cvu"))
            val oldUser = oldAccount.user
            val userPatchValidator = ctx.bodyValidator(UserPatchAdapter::class.java)
            validatePatchUser(userPatchValidator, oldAccount.user.email)
            val updatedUser = updateUser(oldAccount, userPatchValidator.get(), oldUser)
            ctx.status(201)
            ctx.json(UserToFriendlyJSONFormat.parseWithPassword(updatedUser))
        } catch (e: NoSuchElementException) {
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
            ctx.json(Handler(500, "INTERNAL SERVER ERROR", e.message!!))
        }
        
    }

    // Actualiza el User.
    private fun updateUser(oldAccount: Account, userPatchAdapter: UserPatchAdapter, oldUser: User): User {
        digitalWallet.users.remove(oldAccount.user)
        digitalWallet.accounts.remove(oldAccount)

        val updatedUser = User(
            oldUser.idCard, userPatchAdapter.firstName, userPatchAdapter.lastName,
            userPatchAdapter.email, userPatchAdapter.password, oldUser.isAdmin)

        val updatedAccount = Account(updatedUser, oldAccount.cvu)

        updatedAccount.balance = oldAccount.balance
        updatedAccount.appliedLoyalties.addAll(oldAccount.appliedLoyalties)
        updatedAccount.transactions.addAll(oldAccount.transactions)
        digitalWallet.register(updatedUser)
        digitalWallet.assignAccount(updatedUser, updatedAccount)
        return updatedUser
    }

    // Valida si los campos pueden ser reemplazados.
    private fun validatePatchUser(userPatchValidator: Validator<UserPatchAdapter>, oldEmail: String) {
        validateFieldsNotBlank(userPatchValidator)
        validateFieldsFormat(userPatchValidator)
        validateEnableEmail(oldEmail, userPatchValidator)
    }

    // Comprueba que los formatos sean validos
    private fun validateFieldsFormat(userPatchValidator: Validator<UserPatchAdapter>) {
        userPatchValidator.check(
            { it.lastName.chars().allMatch(Character::isLetter) },
            "Lastname field only accept letters.").get()
        userPatchValidator.check(
            { it.firstName.chars().allMatch(Character::isLetter) },
            "Firstname field only accept letters.").get()
        userPatchValidator.check(
            { it.email.matches("^[\\w.-]{1,20}@\\w{3,7}\\.[a-zA-Z]{2,3}$".toRegex()) },
            "Email field only use an email format (name_surname@email.com).").get()
    }

    // Comprueba que el email este disponible.
    private fun validateEnableEmail(oldEmail: String, userPatchValidator: Validator<UserPatchAdapter>) {
        val allEmails = digitalWallet.users.map { user -> user.email }
        val allEmailsFiltered = allEmails.filter { email -> email !== oldEmail }
        userPatchValidator.check(
            { !allEmailsFiltered.contains(it.email) },
            "This email has been already taken, choose other.").get()
    }

    // Comprueba que los campos no esten vacios.
    private fun validateFieldsNotBlank(userPatchValidator: Validator<UserPatchAdapter>) {
        userPatchValidator.check({ !it.firstName.isBlank() }, "Firstname field can`t be blank.").get()
        userPatchValidator.check({ !it.lastName.isBlank() }, "Lastname field can`t be blank.").get()
        userPatchValidator.check({ !it.email.isBlank() }, "Email field can`t be blank.").get()
        userPatchValidator.check({ !it.password.isBlank()}, "Password can't be blank.").get()
    }
}
