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
class DELETEUsersSpec {
    private lateinit var api: Javalin

    @BeforeAll
    fun setUp() {
        api = DigitalWalletAPI(7000, DigitalWallet()).init()
        FuelManager.instance.basePath = "http://localhost:${api.port()}/"
        val aryaJson = """{"idCard": "23492349","lastname": "Stark","firstname": "Arya","email": "arya_stark@outlook.com","password": "arya123"}"""
        Fuel.post("users").jsonBody(aryaJson).responseObject<String>()
        val dannyJson = """{"idCard": "76442331","lastname": "Targaryen","firstname": "Daenerys","email": "danny.targaryen@gmail.com","password": "danny123"}"""
        Fuel.post("users").jsonBody(dannyJson).responseObject<String>()
        val transferBody =
            """
                {
                "fromCVU":"806122948",
                "toCVU":"573003891",
                "amount":"200.00"
                }
            """
        Fuel.post("transfer").jsonBody(transferBody).responseObject<String>()
    }

    @AfterAll
    fun tearDown() {
        api.stop()
    }

    @Test @Order(1)
    fun `1 DELETE users removes the specified user`() {
        val (_, responseDelete, resultDelete) = Fuel.delete("users/806122948").responseObject<String>()
        Assertions.assertEquals(204, responseDelete.statusCode)

        val (_, _, resultGet) = Fuel.get("users").responseObject<List<BasicUserAdapter>>()
        Assertions.assertTrue(resultGet.get().none { it.account.cvu == "806122948" })
    }
}