package edu.unq.ar.digital.wallet

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.jackson.responseObject
import edu.unq.ar.digital.wallet.model.adapter.BasicUserAdapter
import io.javalin.Javalin
import org.junit.jupiter.api.*
import wallet.DigitalWallet

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class POSTTransferSpec {
    private lateinit var api: Javalin

    @BeforeAll
    fun setUp() {
        api = DigitalWalletAPI(7000, DigitalWallet()).init()
        // Inject the base path to no have repeat the whole URL
        FuelManager.instance.basePath = "http://localhost:${api.port()}/"
        val aryaJson = """{"idCard": "23492349","lastname": "Stark","firstname": "Arya","email": "arya_stark@outlook.com","password": "arya123"}"""
        Fuel.post("users").jsonBody(aryaJson).responseObject<String>()
        val dannyJson = """{"idCard": "76442331","lastname": "Targaryen","firstname": "Daenerys","email": "danny.targaryen@gmail.com","password": "danny123"}"""
        Fuel.post("users").jsonBody(dannyJson).responseObject<String>()
    }

    @AfterAll
    fun tearDown() {
        api.stop()
    }

    @Test @Order(1)
    fun `1 POST transfer money between users`() {
        val transferBody =
            """
                {
                "fromCVU":"806122948",
                "toCVU":"573003891",
                "amount":"100.00"
                }
            """
        val (_, response, result) = Fuel.post("transfer").jsonBody(transferBody).responseObject<String>()
        Assertions.assertEquals(201, response.statusCode)
        Assertions.assertEquals("CREATED", result.get())

        val (_, _, userList) = Fuel.get("users").responseObject<List<BasicUserAdapter>>()
        val arya: BasicUserAdapter = userList.get().first { it.account.cvu == "806122948" }
        val daenerys: BasicUserAdapter = userList.get().first { it.account.cvu == "573003891"}
        Assertions.assertEquals(100.00, arya.account.balance)
        Assertions.assertEquals(300.00, daenerys.account.balance)
    }
}