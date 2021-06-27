package edu.unq.ar.ui

import edu.unq.ar.ui.view.login.LoginWindow
import edu.unq.ar.ui.viewModel.login.LoginUserViewModel
import edu.unq.ar.ui.viewModel.utilities.DigitalWalletFactory
import org.uqbar.arena.Application
import org.uqbar.arena.windows.Window
import wallet.DigitalWallet

fun main() = DigitalWalletApplication().start()

class DigitalWalletApplication : Application() {
    override fun createMainWindow(): Window<*> {
        val system: DigitalWallet = DigitalWalletFactory.makeDigitalWalletSystem()
        return LoginWindow(this, LoginUserViewModel(system))
    }
}
