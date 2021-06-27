package edu.unq.ar.ui.view.login

import edu.unq.ar.ui.viewModel.login.LoginUserViewModel
import edu.unq.ar.ui.viewModel.utilities.Support
import org.uqbar.arena.kotlin.extensions.caption
import org.uqbar.arena.kotlin.extensions.color
import org.uqbar.arena.kotlin.extensions.text
import org.uqbar.arena.kotlin.extensions.with
import org.uqbar.arena.widgets.Button
import org.uqbar.arena.widgets.Label
import org.uqbar.arena.widgets.Panel
import org.uqbar.arena.windows.Dialog
import org.uqbar.arena.windows.WindowOwner
import java.awt.Color

class LoginDialogException(owner: WindowOwner, model: LoginUserViewModel) : Dialog<LoginUserViewModel>(owner,model) {
    override fun createFormPanel(mainPanel: Panel?) {

        Label(mainPanel) with {
            text = "Usuario o contraseña incorrecta"
            color = Color.RED
            //iconImage = Support.ICON
        }
        Button(mainPanel) with {
            caption = "Aceptar"
            onClick { accept() }
        }
    }

}
