package edu.unq.ar.ui.viewModel.user

import edu.unq.ar.ui.viewModel.transaction.TransactionalViewModel
import org.uqbar.commons.model.annotations.Observable
import wallet.Account

@Observable
class AccountViewModel(var account: Account?) {
    var cvu : String? = account?.cvu ?: " "
    var balance : Double? = account?.balance ?: 0.0
    var transactions = initTransactions()
    var blocked: Boolean? = account?.isBlocked

    private fun initTransactions() = account?.transactions?.map {
        TransactionalViewModel(
            it
        )
    }?.toMutableList()
}
