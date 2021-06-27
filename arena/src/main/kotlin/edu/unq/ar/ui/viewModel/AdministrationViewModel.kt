package edu.unq.ar.ui.viewModel

import edu.unq.ar.ui.viewModel.login.LoginUserViewModel
import edu.unq.ar.ui.viewModel.loyalty.LoyaltyGiftViewModel
import edu.unq.ar.ui.viewModel.user.UserViewModel
import org.uqbar.commons.model.annotations.Observable
import org.uqbar.commons.model.exceptions.UserException
import wallet.Account
import wallet.DigitalWallet
import wallet.LoyaltyGift
import wallet.User

@Observable
class AdministrationViewModel(loginUserViewModel: LoginUserViewModel) {
    var system: DigitalWallet = loginUserViewModel.system
    var loggedUserName = loginUserViewModel.fullName
    var usersInTable: MutableList<UserViewModel> = initUsers()
    var loyaltiesInTable: MutableList<LoyaltyGiftViewModel> = initLoyalties()
    var selectedUserInTable: UserViewModel? = null
    var filter: String? = ""
        set(value) {
            field = value.toString().toLowerCase()
            search()
        }

    private fun initUsers(): MutableList<UserViewModel> = system.users.map { UserViewModel(it, system) }.toMutableList()

    private fun initLoyalties(): MutableList<LoyaltyGiftViewModel> = system.loyaltyGifts.map { LoyaltyGiftViewModel(it) }.toMutableList()

    private fun search() {
        refreshTables()
        usersInTable = usersInTable
                .filter { it.fullName().toLowerCase().contains(filter.toString().toLowerCase()) }.toMutableList()
    }

    fun deleteUser() {
        validateUserDeleted()
        selectedUserInTable?.model().let { system.deleteUser(it!!) }
        search()
    }

    private fun validateUserDeleted() {
        if (!selectedUserCanBeDeleted()) throw UserException("El usuario debe tener un saldo de $0 pesos para poder ser borrado.")
    }

    private fun selectedUserCanBeDeleted(): Boolean = selectedUserInTable?.balance() == 0.0

    fun addUser(user: User) {
        if (emailNotEnable(user.email)) {
            throw UserException("El mail no esta disponible. Intente con otro mail.")
        } else {
            system.register(user)
            system.assignAccount(user, Account(user, DigitalWallet.generateNewCVU()))
            refreshTables()
        }
    }

    fun addLoyalty(loyalty: LoyaltyGift) {
        system.addLoyalty(loyalty)
        refreshTables()
    }

    private fun refreshTables() {
        usersInTable = initUsers()
        loyaltiesInTable = initLoyalties()
    }

    private fun emailNotEnable(email: String) : Boolean {
        return registeredEmails().any { it == email }
    }

    private fun registeredEmails(): List<String> {
        return system.users.map { it.email }
    }
}