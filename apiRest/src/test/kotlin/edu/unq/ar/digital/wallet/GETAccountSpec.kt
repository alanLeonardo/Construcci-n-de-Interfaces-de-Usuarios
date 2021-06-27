package edu.unq.ar.digital.wallet

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.jackson.responseObject
import edu.unq.ar.digital.wallet.model.adapter.AccountAdapter
import io.javalin.Javalin
import org.junit.jupiter.api.*
import wallet.DigitalWallet

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class GETAccountSpec {
    private lateinit var api: Javalin

    @BeforeAll
    fun setUp() {
        api = DigitalWalletAPI(7000, DigitalWallet()).init()
        FuelManager.instance.basePath = "http://localhost:${api.port()}/"
        val aryaJson = """{"idCard": "23492349","lastname": "Stark","firstname": "Arya","email": "arya_stark@outlook.com","password": "arya123"}"""
        Fuel.post("users").jsonBody(aryaJson).responseObject<String>()
    }


    @AfterAll
    fun tearDown() {
        api.stop()
    }

    @Test @Order(1)
    fun `1 GET account return an amount of $200`() {
        val (_, response, result) = Fuel.get("account/806122948").responseObject<AccountAdapter>()
        Assertions.assertEquals(200, response.statusCode)
        Assertions.assertEquals(200.00, result.get().amount)
    }

}