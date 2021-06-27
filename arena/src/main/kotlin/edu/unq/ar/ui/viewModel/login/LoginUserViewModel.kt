package edu.unq.ar.ui.viewModel.login

import org.uqbar.commons.model.annotations.Observable
import org.uqbar.commons.model.exceptions.UserException
import wallet.Account
import wallet.DigitalWallet
import wallet.User
import java.util.regex.Pattern

// View Model para el Ingreso de Usuarios ya registrados al sistema.
@Observable
class LoginUserViewModel(var system: DigitalWallet) {

    var email: String = "jon.snow@gmail.com"
    var password: String = "jsnow"
    var userLogged: User? = null
    var fullName: String = ""

    fun getUserLogged() {
        userLogged = system.users.find { user -> user.email == email }
        fullName = userLogged?.fullName().toString()
    }

    fun login() {
        system.login(email,password)
        getUserLogged()
    }

    fun register(user: User) {
        system.register(user)
        system.assignAccount(user, Account(user, DigitalWallet.generateNewCVU()))
    }

    fun validacionDeLogueo() {
        this.chequeoCamposCompletados()
        this.chequeoFormatoEmail()
        this.chequeoEsAdministrador()
    }

    private fun chequeoCamposCompletados() {
        if (!camposCompletados()) throw UserException("Los datos no fueron completados.")
    }

    private fun chequeoFormatoEmail() {
        if (!validarEmail()) throw UserException("El email tiene que tener un formato tipo nombreEmail@gmail.com")
    }

    private fun chequeoEsAdministrador() {
        if (!validarAdministrador()) throw UserException("Acceso restringido solo para administradores.")
    }

    private fun validarAdministrador(): Boolean = system.getAllAdmins().
        any { admin -> admin.email == this.email && admin.password == this.password }

    fun validarEmail() : Boolean {
        val pattern: Pattern = Pattern
            .compile("""[a-zA-Z0-9+._%\-+]{1,256}\@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+""")
        return  pattern.matcher(email).find()
    }

    // Retorna true si los campos no son null, ni estan vacios.
    private fun camposCompletados() : Boolean = validarEmailNoVacio() && validarPasswordNoVacio()

    private fun validarPasswordNoVacio() = !password.isNullOrBlank()

    private fun validarEmailNoVacio() = !email.isNullOrBlank()
}
