package edu.unq.ar.ui.view.transaction

import edu.unq.ar.ui.viewModel.transaction.TransactionalViewModel
import edu.unq.ar.ui.viewModel.user.UserViewModel
import edu.unq.ar.ui.viewModel.utilities.Support
import org.uqbar.arena.kotlin.extensions.*
import org.uqbar.arena.widgets.Label
import org.uqbar.arena.widgets.Panel
import org.uqbar.arena.windows.Dialog
import org.uqbar.arena.windows.WindowOwner
import java.awt.Color

class TransactionDialog(owner: WindowOwner, model: UserViewModel) : Dialog<UserViewModel>(owner,model){
    override fun createFormPanel(mainPanel: Panel) {
        title = "Movimientos"
        //iconImage = Support.ICON
        subtitle(mainPanel, "Listado de Movimientos", Color.BLUE)

        table<TransactionalViewModel>(mainPanel) with {
            setNumberVisibleRows(10)
            bindItemsTo("account.transactions")
            column {
                setTitle("Monto")
                setFixedSize(200)
                color = Color.DARK_GRAY
                bindContentsTo("amount")
            }
            column {
                setTitle("Fecha y Hora")
                setFixedSize(200)
                bindContentsTo("dateTime")
            }
            column {
                setTitle("Descripción")
                setFixedSize(200)
                bindContentsTo("description")
            }
            column {
                setTitle("Descripción Completa")
                setFixedSize(200)
                bindContentsTo("fullDescription")
            }
            column {
                setTitle("Débitos")
                setFixedSize(200)
                bindContentsTo("esCashOut")
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

}
