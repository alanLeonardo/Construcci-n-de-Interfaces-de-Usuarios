package edu.unq.ar.digital.wallet

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.jackson.responseObject
import edu.unq.ar.digital.wallet.api.handler.Handler
import io.javalin.Javalin
import jdk.nashorn.internal.parser.JSONParser
import org.junit.jupiter.api.*
import wallet.DigitalWallet
import kotlin.test.assertFailsWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class POSTRegisterSpec {

    private lateinit var api: Javalin

    @BeforeAll
    fun setUp() {
        api = DigitalWalletAPI(7000, DigitalWallet()).init()
        FuelManager.instance.basePath = "http://localhost:${api.port()}/"
        val dannyJson = """{"idCard": "76442331","lastname": "Targaryen","firstname": "Daenerys","email": "danny.targaryen@gmail.com","password": "danny123"}"""
        Fuel.post("users").jsonBody(dannyJson).responseObject<String>()
    }

    @AfterAll
    fun tearDown() {
        api.stop()
    }

    @Test @Order(1)
    fun `1 POST user store a new user`() {
        val userJson =
            """
            {
                "idCard": "23492349",
	            "lastname": "Stark",
	            "firstname": "Arya",
	            "email": "arya_stark@outlook.com",
	            "password": "arya123"
            }
            """
        val (_, response, _) = Fuel.post("users").jsonBody(userJson).responseObject<String>()
        Assertions.assertEquals(201, response.statusCode)
    }

    @Test @Order(2)
    fun `2 POST user registered twice then status is 409 Conflict`(){
        val aryaJson = """{"idCard": "23492349","lastname": "Stark","firstname": "Arya","email": "arya_stark@outlook.com","password": "arya123"}"""
        Fuel.post("users").jsonBody(aryaJson).responseObject<String>()
        val (_, response, result) = Fuel.post("users").jsonBody(aryaJson).responseObject<Handler>()
        Assertions.assertEquals(409, response.statusCode)
        val (_ , error) = result // Al ser status 4.x.x, Fuel usa FuelError.
        Assertions.assertEquals("HTTP Exception 409 Conflict", error!!.message)
    }
}