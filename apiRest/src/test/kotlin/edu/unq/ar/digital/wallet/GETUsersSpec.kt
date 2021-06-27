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
class GETUsersSpec {
    private lateinit var api: Javalin

    @BeforeAll
    fun setUp() {
        api = DigitalWalletAPI(7000, DigitalWallet()).init()
        FuelManager.instance.basePath = "http://localhost:${api.port()}/"
    }

    @AfterAll
    fun tearDown() {
        api.stop()
    }

    @Test @Order(1)
    fun `1 GET users retrieves an empty list by default`() {
        val (_, response, result) = Fuel.get("users").responseObject<List<BasicUserAdapter>>()
        Assertions.assertEquals(200, response.statusCode)
        Assertions.assertEquals(0, result.get().size)
        Assertions.assertTrue(result.get().isEmpty())
    }

    @Test @Order(2)
    fun `2 GET users includes previous user storing`() {
        val userJson = """{"idCard": "23492349","lastname": "Stark","firstname": "Arya","email": "arya_stark@outlook.com","password": "arya123"}"""
        Fuel.post("users").jsonBody(userJson).responseObject<String>()

        val (_, response, result) = Fuel.get("users").responseObject<List<BasicUserAdapter>>()
        Assertions.assertEquals(200, response.statusCode)
        Assertions.assertEquals("arya_stark@outlook.com", result.get().first().email)
        Assertions.assertEquals("23492349", result.get().first().idCard)
        Assertions.assertEquals(200.00, result.get().first().account.balance)
    }

}