package edu.unq.ar.ui.viewModel.user

import org.uqbar.commons.model.annotations.TransactionalAndObservable
import org.uqbar.commons.model.exceptions.UserException
import wallet.DigitalWallet
import wallet.User
import java.util.regex.Pattern

// View Model para los Usuarios para crear.
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@TransactionalAndObservable
class UserViewModel(var model: User?, var system: DigitalWallet) {

    // SHOW IN TABLE / EDIT / VIEW USER
    var userAppModel = model
    var account = AccountViewModel(model?.account)
    var accountBlocked = mutableListOf("Habilitado","Bloqueado")
    var idCard = model?.idCard
    var firstName = model?.firstName
    var lastName = model?.lastName
    var email = model?.email
    var password = model?.password
    var esAdmin = model?.isAdmin
    var state = this.getStateOfAccount()

    // NEW USER
    constructor(system: DigitalWallet) : this(null, system) {
        idCard = null
        firstName = null
        lastName = null
        email = null
        password = null
        esAdmin = false
    }

    private fun getStateOfAccount(): String {
        return if (account.blocked == null) {
            ""
        } else {
            if (account.blocked!!) {
                "Habilitado"
            } else {
                "Bloqueado"
            }
        }
    }

    fun model(): User? {
        return this.userAppModel
    }

    fun fullName() = (this.firstName+" "+this.lastName)

    // Valida si los campos fueron ingresados correctamente.
    // En caso contrario, lanza una excepcion.
    // Usado para los User nuevos.
    fun validacionDeRegistro() {
        this.chequeoValidacionNombre()
        this.chequeoValidacionApellido()
        this.validacionDeDocumento()
        this.chequeoValidacionEmail()
        this.chequeoFormatoEmail()
        this.chequeoDisponibilidadEmail()
        this.chequeoValidacionPassword()
    }

    private fun validacionDeDocumento() {
        this.chequeoValidacionDocumento()
        this.chequeoDeFormatoDeDocumento()
    }

    private fun chequeoNombreCompletado() {
        if (!validarNombreNoVacio()) throw UserException("El campo de Nombre debe ser completado.")
    }
    private fun chequeoApellidoCompletado() {
        if (!validarApellidoNoVacio()) throw UserException("El campo de Apellido debe ser completado.")
    }

    // Usado para los User editados.
    fun validacionDeModificacionDeEmail() {
        this.validarEmailNoVacio()
        this.chequeoFormatoEmail()
        this.chequeoDisponibilidadDeEmailModificado()
    }

    private fun chequeoDisponibilidadDeEmailModificado() {
        if (elEmailModificadoEstaOcupado()) throw UserException("El email no esta disponible. Por favor, elija otro.")
    }

    private fun chequeoDisponibilidadEmail() {
        if (elEmailEstaOcupado()) throw UserException("El email no esta disponible. Por favor, elija otro.")
    }

    private fun elEmailEstaOcupado(): Boolean = system.users.any { it.email === this.email }

    private fun elEmailModificadoEstaOcupado(): Boolean {
        return system.users.any { it.email === this.email && it.idCard !== this.idCard }
    }
    private fun chequeoFormatoEmail() {
        if (!validarEmail()) throw UserException("El email tiene que tener un formato tipo nombreEmail@gmail.com")
    }

    private fun chequeoDeFormatoDeDocumento() {
        if (this.idCard?.length!! != 8) throw UserException("El documento debe tener un formato de numeros 8.")
    }

    private fun chequeoValidacionDocumento() {
        if (!validarIDCardNoVacio()) throw UserException("Usted debe indicar su numero de documento.")
    }

    private fun chequeoValidacionNombre() {
        if (!validarNombreNoVacio()) throw UserException("Usted debe indicar su nombre.")
    }

    private fun chequeoValidacionApellido() {
        if (!validarApellidoNoVacio()) throw UserException("Usted debe indicar su apellido.")
    }

    private fun chequeoValidacionEmail() {
        if (!validarEmailNoVacio()) throw UserException("Usted debe indicar su Email.")
    }

    private fun chequeoValidacionPassword() {
        if (!validarPasswordNoVacio()) throw UserException("Usted debe indicar su password.")
    }

    private fun validarPasswordNoVacio() = !password.isNullOrBlank()

    private fun validarEmailNoVacio() = !email.isNullOrBlank()

    private fun validarApellidoNoVacio() = !lastName.isNullOrBlank()

    private fun validarNombreNoVacio() = !firstName.isNullOrBlank()

    private fun validarIDCardNoVacio() = !idCard.isNullOrBlank()


    fun validarEmail() : Boolean {
        val pattern: Pattern = Pattern
                .compile("""[a-zA-Z0-9+._%\-+]{1,256}\@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+""")
        return  pattern.matcher(email).find()
    }

    fun createModel(): User = User(idCard!!, firstName!!, lastName!!, email!!, password!!, esAdmin!!)

    fun balance(): Double? = this.account.balance
}
