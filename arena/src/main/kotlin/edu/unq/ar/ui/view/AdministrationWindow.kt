package edu.unq.ar.ui.view

import edu.unq.ar.ui.view.addUser.AddUserDialog
import edu.unq.ar.ui.view.deleteUser.DeleteUserDialog
import edu.unq.ar.ui.view.editUser.EditUserDialog
import edu.unq.ar.ui.view.infoUser.InfoUserDialog
import edu.unq.ar.ui.view.login.LoginWindow
import edu.unq.ar.ui.view.loyalty.LoyaltyDialog
import edu.unq.ar.ui.viewModel.AdministrationViewModel
import edu.unq.ar.ui.viewModel.login.LoginUserViewModel
import edu.unq.ar.ui.viewModel.loyalty.LoyaltyGiftViewModel
import edu.unq.ar.ui.viewModel.user.UserViewModel
import edu.unq.ar.ui.viewModel.utilities.Support
import org.uqbar.arena.kotlin.extensions.*
import org.uqbar.arena.widgets.Button
import org.uqbar.arena.widgets.Label
import org.uqbar.arena.widgets.Panel
import org.uqbar.arena.widgets.TextBox
import org.uqbar.arena.windows.SimpleWindow
import org.uqbar.arena.windows.WindowOwner
import org.uqbar.commons.model.exceptions.UserException
import org.uqbar.lacar.ui.model.ControlBuilder
import wallet.User
import java.awt.Color
import kotlin.NullPointerException

class AdministrationWindow(owner: WindowOwner, model: AdministrationViewModel): SimpleWindow<AdministrationViewModel>(owner, model) {
    override fun addActions(actionsPanel: Panel) {}
    override fun createFormPanel(mainPanel: Panel) {
        setMinWidth(720)
        title = "DigitalWallet - Administración"
       // iconImage = Support.ICON
        loggedUserPanel(mainPanel)
        subtitle(mainPanel, "Listado de Usuarios", Color.BLUE)
        TextBox(mainPanel) with {
            bindTo("filter")
        }
        usersTable(mainPanel)
        buttonsPanel(mainPanel, modelObject)
        subtitle(mainPanel, "Listado de Beneficios", Color.RED)
        loyaltiesTable(mainPanel)
        loyaltyButton(mainPanel)
    }

    private fun loggedUserPanel(mainPanel: Panel) {
        Panel(mainPanel) with {
            asHorizontal()
            Label(it).setText("Bienvenido, ").fontSize = 12
            Label(it).setFontSize(12).bindValueToProperty<AdministrationViewModel, ControlBuilder>("loggedUserName")
            Button(it) with {
                setCaption("Cerrar Sesion")
                onClick {
                    close()
                    LoginWindow(owner, LoginUserViewModel(thisWindow.modelObject.system)).open()
                }
            }
        }
    }

    private fun loyaltyButton(mainPanel: Panel) {
        Button(mainPanel) with {
            setCaption("Agregar Beneficio")
            width = 160
            setBackground(Color.RED)
            setForeground(Color.WHITE)
            onClick {
                LoyaltyDialog(owner, LoyaltyGiftViewModel()) with {
                    onAccept {
                        this@AdministrationWindow.modelObject.addLoyalty(modelObject.createModel())
                    }
                    open()
                }
            }
        }
    }

    private fun subtitle(mainPanel: Panel, subtitle: String, aColor: Color) {
        Label(mainPanel) with {
            setText(subtitle)
            fontSize = 18
            alignCenter()
            setBackground(aColor)
            setForeground(Color.WHITE)
        }
    }

    private fun buttonsPanel(mainPanel: Panel, model: AdministrationViewModel) {
        Panel(mainPanel) with {
            it.asHorizontal()
            Button(it) with {
                setCaption("Ver")
                setWidth(220)
                setBackground(Color.BLUE)
                setForeground(Color.WHITE)
                onClick {
                    try {
                        InfoUserDialog(owner, model.selectedUserInTable!!).open()
                    } catch (e: NullPointerException) {
                        throw UserException("Seleccione al usuario que desea ver detalladamente.")
                    }
                }
            }
            Button(it) with {
                setCaption("Agregar")
                setWidth(220)
                setBackground(Color.BLUE)
                setForeground(Color.WHITE)
                onClick {
                    val newUser = UserViewModel(model.system)
                    AddUserDialog(owner, newUser) with {
                        onAccept {
                            val user = modelObject.createModel()
                            addUser(user)
                        }
                        open()
                    }
                }
            }
            Button(it) with {
                setCaption("Modificar")
                setWidth(220)
                setBackground(Color.BLUE)
                setForeground(Color.WHITE)
                onClick {
                    try {
                        EditUserDialog(owner, model.selectedUserInTable!!) with {
                            onAccept {

                            }
                            open()
                        }
                    } catch (e: NullPointerException) {
                        throw UserException("Seleccione al usuario que desea modificar.")
                    }
                }
            }

            Button(it) with {
                setCaption("Eliminar")
                setWidth(220)
                setBackground(Color.BLUE)
                setForeground(Color.WHITE)
                onClick {
                    if (model.selectedUserInTable !== null) {
                        DeleteUserDialog(owner, model).open()
                    } else {
                        throw UserException("Seleccione al usuario que desea eliminar.")
                    }
                }
            }
        }
    }

    private fun addUser(user: User) {
        modelObject.addUser(user)
    }

    private fun loyaltiesTable(mainPanel: Panel) {
        table<LoyaltyGiftViewModel>(mainPanel) with {
            setNumberVisibleRows(5)
            bindItemsTo("loyaltiesInTable")
            column {
                setTitle("Nombre")
                setFixedSize(200)
                bindContentsTo("nameLoyalty")
            }
            column {
                setTitle("Estrategia")
                setFixedSize(100)
                bindContentsTo("strategy")
            }
            column {
                setTitle("Desde")
                setFixedSize(100)
                bindContentsTo("validFrom")
            }
            column {
                setTitle("Hasta")
                setFixedSize(100)
                bindContentsTo("validTo")
            }
            column {
                setTitle("Minimo Monto por Transaccion")
                setFixedSize(200)
                bindContentsTo("minAmountPerTransaction")
            }
            column {
                setTitle("Minima Cantidad de Transacciones")
                setFixedSize(200)
                bindContentsTo("minNumberOfTransactions")
            }
        }
    }

    private fun usersTable(mainPanel: Panel) {
        table<UserViewModel>(mainPanel) with {
            setNumberVisibleRows(5)
            bindItemsTo("usersInTable")
            bindSelectionTo("selectedUserInTable")
            column {
                setTitle("Nombre")
                setMinWidth(200)
                bindContentsTo("firstName")
            }
            column {
                setTitle("Apellido")
                setMinWidth(200)
                bindContentsTo("lastName")
            }
            column {
                setTitle("Correo Electronico")
                setMinWidth(200)
                bindContentsTo("email")
            }
            column {
                setTitle("Estado de Cuenta Bancaria")
                setMinWidth(200)
                bindContentsTo("state")
            }
        }
    }


}
