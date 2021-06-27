package edu.unq.ar.ui.view.editUser

import edu.unq.ar.ui.viewModel.user.UserViewModel
import edu.unq.ar.ui.viewModel.utilities.EmailFilter
import edu.unq.ar.ui.viewModel.utilities.Support
import org.uqbar.arena.aop.windows.TransactionalDialog
import org.uqbar.arena.kotlin.extensions.*
import org.uqbar.arena.widgets.*
import org.uqbar.arena.windows.WindowOwner
import org.uqbar.commons.model.exceptions.UserException

class EditUserDialog(owner: WindowOwner?, model: UserViewModel) : TransactionalDialog<UserViewModel>(owner, model) {

    override fun createFormPanel(mainPanel: Panel?) {
        title = "DigitalWallet - Modificar Usuario"
        //iconImage = Support.ICON

        Panel(mainPanel) with {
            asColumns(2)

            Label(it) withText "Nombre:"
            Label(it) with {
                bindTo("firstName")
            }

            Label(it) withText "Apellido:"
            Label(it) with {
                bindTo("lastName")
            }

            Label(it) withText "Número de Documento:"
            Label(it) with {
                bindTo("idCard")
            }

            Label(it) withText "Correo Electronico:"
            TextBox(it) with {
                bindTo("email")
                withFilter(EmailFilter())
            }

            Label(it) withText "Estado:"
            Selector<Boolean>(it) with {
                bindItemsTo("accountBlocked")
                bindTo("state")
            }

            Label(it) withText "CVU:"
            Label(it) with {
                bindTo("account.cvu")
            }

            Label(it) withText "Saldo:"
            Label(it) with {
                bindTo("account.balance")
            }
        }

        Button(mainPanel) with {
            caption = "Aceptar"
            onClick {
                try {
                    modelObject.validacionDeModificacionDeEmail()
                    accept()
                } catch (e: UserException) {
                    throw UserException(e.message)
                }
            }
        }
    }

}
