package edu.unq.ar.digital.wallet

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.jackson.responseObject
import io.javalin.Javalin
import org.junit.jupiter.api.*
import wallet.DigitalWallet

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class POSTLoginSpec {

    private lateinit var api: Javalin

    @BeforeAll
    fun setUp() {
        api = DigitalWalletAPI(7000, DigitalWallet()).init()
        FuelManager.instance.basePath = "http://localhost:${api.port()}/"
        val userJson = """{"idCard": "23492349","lastname": "Stark","firstname": "Arya","email": "arya_stark@outlook.com","password": "arya123"}"""
        Fuel.post("users").jsonBody(userJson).responseObject<String>()
    }

    @AfterAll
    fun tearDown() {
        api.stop()
    }

    @Test @Order(1)
    fun `1 POST login accept user stored`() {
        val body =
            """
            {
	            "email": "arya_stark@outlook.com",
	            "password": "arya123"
            }
            """
        val (_, response, result) = Fuel.post("login").jsonBody(body).responseObject<String>()
        Assertions.assertEquals(200, response.statusCode)
    }
}