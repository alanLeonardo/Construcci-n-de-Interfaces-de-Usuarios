package edu.unq.ar.ui.viewModel.utilities

import org.uqbar.arena.filters.TextFilter
import org.uqbar.arena.widgets.TextInputEvent

class EmailFilter() : TextFilter {

    override fun accept(event: TextInputEvent): Boolean {
        return event.potentialTextResult.matches("^[\\w.-]{0,20}@{0,1}\\w{0,7}\\.{0,1}[a-zA-Z]{0,3}$".toRegex())
    }
}
