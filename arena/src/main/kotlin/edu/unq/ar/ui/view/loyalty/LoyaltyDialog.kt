package edu.unq.ar.ui.view.loyalty

import edu.unq.ar.ui.viewModel.utilities.DateTextFilter
import edu.unq.ar.ui.viewModel.utilities.DateTransformer
import edu.unq.ar.ui.viewModel.loyalty.LoyaltyGiftViewModel
import edu.unq.ar.ui.viewModel.utilities.Support
import org.uqbar.arena.kotlin.extensions.*
import org.uqbar.arena.widgets.*
import org.uqbar.arena.windows.Dialog
import org.uqbar.arena.windows.WindowOwner
import wallet.LoyaltyGiftStrategy

class LoyaltyDialog(owner: WindowOwner?, model: LoyaltyGiftViewModel): Dialog<LoyaltyGiftViewModel>(owner, model) {
    override fun createFormPanel(mainPanel: Panel?) {
        title = "DigitalWallet - Administracion de Beneficios"
        //iconImage = Support.ICON

        setMinWidth(415)
        Panel(mainPanel) with {
            asColumns(2)

            Label(it) withText "Nombre" align "Left"
            TextBox(it) with {
                bindTo("nameLoyalty")
                setWidth(170)
            }

            Label(it) withText "Fecha Desde" align "Left"
            TextBox(it) with {
                setWidth(170)
                withFilter(DateTextFilter())
                bindTo("validFrom").setTransformer(DateTransformer())
            }

            Label(it) withText "Fecha Hasta" align "Left"
            TextBox(it) with {
                setWidth(170)
                withFilter(DateTextFilter())
                bindTo("validTo").setTransformer(DateTransformer())
            }

            Label(it) withText "Tipo de Descuento" align "Left"
            Selector<LoyaltyGiftStrategy>(it) with {
                setWidth(170)
                bindItemsTo("switchStrategy")
                bindTo("strategy")
            }

            Label(it) withText "Importe del regalo" align "Left" bindVisibleTo "isGiftStrategy"
            TextBox(it) with {
                bindVisibleTo("isGiftStrategy")
                setWidth(170)
                bindTo("amount")
                withFilter { event -> event.potentialTextResult.matches(Regex("^[0-9]{0,9}$")) || event.potentialTextResult.length == 9 }
            }

            Label(it) withText "Porcentaje del descuento" align "Left" bindVisibleTo "isDiscountStrategy"
            TextBox(it) with {
                bindVisibleTo("isDiscountStrategy")
                setWidth(170)
                bindTo("percentage")
                withFilter { event -> event.potentialTextResult.matches(Regex("^[0-9]\$|^[1-9][0-9]\$|^(100)\$")) || event.potentialTextResult.length == 2 }
            }

            Label(it) withText "Cantidad de operaciones" align "Left"
            TextBox(it) with {
                setWidth(170)
                bindTo("minNumberOfTransactions")
                withFilter { event -> event.potentialTextResult.matches(Regex("^[0-9]{0,9}$")) || event.potentialTextResult.length == 9 }
            }

            Label(it) withText "Importe de cada operacion" align "Left"
            TextBox(it) with {
                setWidth(170)
                bindTo("minAmountPerTransaction")
                withFilter { event -> event.potentialTextResult.matches(Regex("^[0-9]{0,9}$")) || event.potentialTextResult.length == 9 }
            }

            Button(it) with {
                setWidth(170)
                setCaption("Cancelar")
                onClick {
                    cancel()
                }
            }

            Button(it) with {
                setWidth(170)
                setCaption("Agregar")
                onClick {
                    checkFields(thisWindow.modelObject)
                    accept()
                }
            }
        }
    }

    private fun checkFields(modelObject: LoyaltyGiftViewModel) {
        modelObject.checkFields()
    }

}
