package edu.unq.ar.ui.view.infoUser

import edu.unq.ar.ui.view.transaction.TransactionDialog
import edu.unq.ar.ui.viewModel.user.UserViewModel
import edu.unq.ar.ui.viewModel.utilities.Support
import org.uqbar.arena.windows.WindowOwner
import org.uqbar.arena.kotlin.extensions.*
import org.uqbar.arena.widgets.*
import org.uqbar.arena.windows.Dialog

class InfoUserDialog(owner: WindowOwner, model: UserViewModel): Dialog<UserViewModel>(owner,model) {

    override fun createFormPanel(mainPanel: Panel?) {
        title = "DigitalWallet -  Ver Usuario"
        //iconImage = Support.ICON

        Panel(mainPanel) with {
            asColumns(2)

            Label(it) withText  "Nombre:"

            Label(it) with {
                bindTo("firstName")
            }

            Label(it) withText  "Apellido:"

            Label(it) with {
                bindTo("lastName")
            }

            Label(it) withText  "Número de Documento:"

            Label(it) with {
                bindTo("idCard")
            }

            Label(it) withText  "Correo Electronico:"

            Label(it) with {
                bindTo("email")
            }

            Label(it) withText  "Estado:"

            Label(it) with {
                bindTo("state")
            }

            Label(it) withText  "CVU:"

            Label(it) with {
                bindTo("account.cvu")
            }

            Label(it) withText  "Saldo:"

            Label(it) with {
                bindTo("account.balance")
            }

            Label(it) withText "Ver Movimientos:"
            Button(it) with {
                caption = "Movimientos"
                onClick {
                    TransactionDialog(owner, thisWindow.modelObject).open()
                }
            }
        }
        Button(mainPanel) with {
            caption = "Aceptar"
            onClick { close() }
        }
    }

}
