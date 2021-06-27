package edu.unq.ar.ui.view.login

import edu.unq.ar.ui.view.addUser.AddUserDialog
import edu.unq.ar.ui.viewModel.login.LoginUserViewModel
import edu.unq.ar.ui.viewModel.user.UserViewModel
import edu.unq.ar.ui.viewModel.utilities.EmailFilter
import edu.unq.ar.ui.viewModel.utilities.Support
import org.uqbar.arena.kotlin.extensions.*
import org.uqbar.arena.widgets.*
import org.uqbar.arena.windows.SimpleWindow
import org.uqbar.arena.windows.WindowOwner
import wallet.LoginException
import wallet.User
import java.awt.Color

class LoginWindow(owner: WindowOwner, model: LoginUserViewModel): SimpleWindow<LoginUserViewModel>(owner, model) {

    override fun addActions(actionsPanel: Panel?) {}

    override fun createFormPanel(mainPanel: Panel?) {
        title = "DigitalWallet - Login"
        //iconImage = Support.ICON
        setMinWidth(300)
        Label(mainPanel) with {
            text = "Bienvenidos a Unquipay"
            alignCenter()
            color = Color.BLUE
            fontSize = 16
        }
        Panel(mainPanel) with {
            asHorizontal()
            Label(it) withText "Correo eletrónico:"
            TextBox(it) with {
                bindTo("email")
                width = 200
                withFilter(EmailFilter())
            }
        }
        Panel(mainPanel) with {
            asHorizontal()
            Label(it) withText "Contraseña:          "
            PasswordField(it) with {
                bindTo("password")
                width = 200
            }
        }
        Button(mainPanel) with {
            caption = "Ingresar"
            onClick {
                try {
                    modelObject.validacionDeLogueo()
                    modelObject.login()
                }catch (e: LoginException){
                    LoginDialogException(thisWindow, modelObject).open()
                    throw LoginException(e.message)
                }
                thisWindow.close()
                LoginDialogOk(thisWindow, modelObject).open()
            }
        }
        Button(mainPanel) with {
            caption = "Registrarse"
            onClick {
                val userToSignUp: UserViewModel = UserViewModel(modelObject.system)
                AddUserDialog(thisWindow, userToSignUp) with {
                    onAccept {
                        val user: User = modelObject.createModel()
                        register(user)
                    }
                    open()
                }
            }
        }
    }

    fun register(user: User) {
        modelObject.register(user)
    }
}
