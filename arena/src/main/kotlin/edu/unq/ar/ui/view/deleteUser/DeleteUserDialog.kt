package edu.unq.ar.ui.view.deleteUser

import edu.unq.ar.ui.viewModel.AdministrationViewModel
import org.uqbar.arena.kotlin.extensions.caption
import org.uqbar.arena.kotlin.extensions.text
import org.uqbar.arena.kotlin.extensions.with
import org.uqbar.arena.widgets.Button
import org.uqbar.arena.widgets.Label
import org.uqbar.arena.widgets.Panel
import org.uqbar.arena.windows.Dialog
import org.uqbar.arena.windows.WindowOwner
import org.uqbar.commons.model.exceptions.UserException

class DeleteUserDialog(owner: WindowOwner, model: AdministrationViewModel): Dialog<AdministrationViewModel>(owner,model) {
    override fun createFormPanel(mainPanel: Panel?) {
        title = "DigitalWallet - Eliminar Usuario"
        //iconImage = Support.ICON
        setMinWidth(350)
        Label(mainPanel) with {
            text = "Seguro que desea eliminar al usuario ${modelObject.selectedUserInTable?.fullName()}"
            fontSize = 10
        }
        Button(mainPanel) with {
            caption = "Aceptar"
            onClick {
                try {
                    modelObject.deleteUser()
                    accept()
                } catch (e: Exception) {
                    throw UserException(e.message)
                }
            }
        }
        Button(mainPanel) with {
            caption = "Cancelar"
            onClick { cancel() }
        }
    }

}
