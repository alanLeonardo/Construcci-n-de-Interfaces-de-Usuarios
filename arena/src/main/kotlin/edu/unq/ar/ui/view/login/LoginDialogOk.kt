package edu.unq.ar.ui.view.login

import edu.unq.ar.ui.view.AdministrationWindow
import edu.unq.ar.ui.viewModel.AdministrationViewModel
import edu.unq.ar.ui.viewModel.login.LoginUserViewModel
import edu.unq.ar.ui.viewModel.utilities.Support
import org.uqbar.arena.kotlin.extensions.caption
import org.uqbar.arena.kotlin.extensions.text
import org.uqbar.arena.kotlin.extensions.thisWindow
import org.uqbar.arena.kotlin.extensions.with
import org.uqbar.arena.widgets.Button
import org.uqbar.arena.widgets.Label
import org.uqbar.arena.widgets.Panel
import org.uqbar.arena.windows.Dialog
import org.uqbar.arena.windows.WindowOwner

class LoginDialogOk(owner: WindowOwner, model: LoginUserViewModel) : Dialog<LoginUserViewModel>(owner,model) {
    override fun createFormPanel(mainPanel: Panel?) {
        title = "                  UNQUIPay"
        //iconImage = Support.ICON

        Label(mainPanel) with {
            text = "Bienvenido!! ${modelObject.fullName}"
            fontSize = 15
        }
        Button(mainPanel) with {
            caption = "Aceptar"
            onClick {
                accept()
                AdministrationWindow(thisWindow, AdministrationViewModel(thisWindow.modelObject)).open()
            }
        }
    }

}
