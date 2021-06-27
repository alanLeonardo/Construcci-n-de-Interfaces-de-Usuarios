package edu.unq.ar.digital.wallet

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.jackson.responseObject
import edu.unq.ar.digital.wallet.model.adapter.BasicTransactionAdapter
import io.javalin.Javalin
import org.junit.jupiter.api.*
import wallet.DigitalWallet

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class GETTransactionsSpec {
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
    fun `1 GET transactions returns a body with a list of transactions`() {
        val (_, response, result) = Fuel.get("transactions/806122948").responseObject<List<BasicTransactionAdapter>>()
        Assertions.assertEquals(200, response.statusCode)
        Assertions.assertEquals(1, result.get().size)
        val firstTransaction = result.get().first()
        Assertions.assertEquals(200.00, firstTransaction.amount)
        Assertions.assertEquals("Regalo de bienvenida por $200.0", firstTransaction.fullDescription)
    }
}