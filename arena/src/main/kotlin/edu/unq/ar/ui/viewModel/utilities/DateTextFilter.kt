package edu.unq.ar.ui.viewModel.utilities

import org.uqbar.arena.filters.TextFilter
import org.uqbar.arena.widgets.TextInputEvent

class DateTextFilter : TextFilter {
    override fun accept(event: TextInputEvent): Boolean {
        val expected = listOf(
                "\\d", "\\d?", "/", "\\d", "\\d?", "/", "\\d{0,4}"
        )
        val regex = expected.reversed()
                .fold("") { accum, elem -> "($elem$accum)?" }
                .toRegex()
        return event.potentialTextResult.matches(regex)
    }
}