package edu.unq.ar.digital.wallet

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.jackson.responseObject
import edu.unq.ar.digital.wallet.api.handler.Handler
import io.javalin.Javalin
import org.junit.jupiter.api.*
import wallet.DigitalWallet

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class POSTCashinSpec {
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
    fun `1 POST cashin returns 201 CREATED`() {
        val transferJson =
            """
                {
                    "fromCVU": "806122948",
	                "amount" : 100.00,
	                "cardNumber":"1234 1234 1234 1234",
	                "fullName":"Arya Stark",
	                "endDate":"07/2019",
	                "securityCode": "123",
	                "isCreditCard": true
                }
            """
        val (_, response, _) = Fuel.post("cashin").jsonBody(transferJson).responseObject<String>()
        Assertions.assertEquals(201, response.statusCode)
    }
}