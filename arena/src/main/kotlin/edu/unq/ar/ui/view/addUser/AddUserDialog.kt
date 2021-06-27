package edu.unq.ar.ui.view.addUser

import edu.unq.ar.ui.viewModel.user.UserViewModel
import edu.unq.ar.ui.viewModel.utilities.EmailFilter
import edu.unq.ar.ui.viewModel.utilities.Support
import org.uqbar.arena.kotlin.extensions.*
import org.uqbar.arena.widgets.*
import org.uqbar.arena.windows.Dialog
import org.uqbar.arena.windows.WindowOwner
import org.uqbar.commons.model.exceptions.UserException

class AddUserDialog(owner: WindowOwner, model: UserViewModel) : Dialog<UserViewModel>(owner, model) {

    override fun createFormPanel(mainPanel: Panel) {
        this.title = "DigitalWallet - Agregar Usuario"
        //iconImage = Support.ICON
        setMinWidth(415)
        Panel(mainPanel) with {
            asColumns(2)
            Label(it) withText ("Nombre") align "Left"
            TextBox(it) with {
                bindTo("firstName")
                width = 200
            }

            Label(it) withText ("Apellido") align "Left"
            TextBox(it) with {
                bindTo("lastName")
                width = 200
            }

            Label(it) withText ("Numero de Documento") align "Left"
            TextBox(it) with {
                bindTo("idCard")
                width = 200
                withFilter { event -> event.potentialTextResult.matches(Regex("^[0-9]{0,8}$")) }
            }

            Label(it) withText ("Correo Electronico")
            TextBox(it) with {
                bindTo("email")
                width = 200
                withFilter(EmailFilter())
            }

            Label(it) withText ("Contraseña")
            TextBox(it) with {
                bindTo("password")
                width = 200
            }

            Label(it) withText ("Es Administrador?")
            CheckBox(it) with {
                bindTo("esAdmin")
                width = 50
            }

            Button(mainPanel) with {
                caption = "Agregar"
                onClick {
                    try {
                        thisWindow.modelObject.validacionDeRegistro()
                        accept()
                    } catch (exceptionDeRegistro: Exception) {
                        throw UserException(exceptionDeRegistro.message)
                    }
                }
            }
        }
    }
}
