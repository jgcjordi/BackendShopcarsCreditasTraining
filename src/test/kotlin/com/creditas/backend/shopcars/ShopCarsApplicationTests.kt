package com.creditas.backend.shopcars

import com.creditas.backend.shopcars.application.domain.entities.Customer
import com.creditas.backend.shopcars.application.services.implementation.CustomerServiceImpl
import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.domain.entities.Model
import com.creditas.backend.shopcars.cars.services.implementation.BrandServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.CarServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.ModelServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.awt.print.Pageable
import java.time.LocalDate

@SpringBootTest
class ShopCarsApplicationTests {
    @Autowired
    private lateinit var customerService: CustomerServiceImpl

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var carService: CarServiceImpl

    @Autowired
    private lateinit var modelService: ModelServiceImpl

    @Autowired
    private lateinit var brandService: BrandServiceImpl

    private val carsEndPoint = "/api/v1/cars"
    private val customerEndPoint = "/api/v1/customers"

    private val mockMvc: MockMvc by lazy {//inicializacion perezosa, sera inicializada cuando se necesitada, bajo demanda "by".
        //configura el mockMvc para que cada vez que haga una peticion http la imprima en la consola.
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
    }

    //para poder aceptar parametros genericos tiene que ser una funcion inline
    private inline fun <reified T> ResultActions.bodyTo(mapper: ObjectMapper = jacksonObjectMapper()): T {
        println(this)
        return mapper.readValue(this.andReturn().response.contentAsByteArray)
    }

    fun MockHttpServletRequestBuilder.body(data: Any, mapper: ObjectMapper = jacksonObjectMapper(), mediaType: MediaType = MediaType.APPLICATION_JSON_UTF8): MockHttpServletRequestBuilder {
        return this.content(mapper.writeValueAsBytes(data)).contentType(mediaType)
    }

    fun doLogin(): String {
        val customer = Customer(name = "Creditas", surname = "Creditas", identification = "12345A", birthday = LocalDate.of(2017, 1, 13), email = "creditas@creditas.com", password = "creditas")

        var bearer = mockMvc.perform(MockMvcRequestBuilders.post("$customerEndPoint/login")
                .body(data = customer, mapper = mapper))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
        return bearer;
    }

    @Test
    fun findAllCarsNoPurchased() {
        val carsFromService = carService.findAllCarsNoPurchased(1)
        val carString: String = mapper.writeValueAsString(carsFromService)

        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/open?page=1"))
                .andExpect(MockMvcResultMatchers.status().isOk)

        val x = mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/open?page=1"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn().response.contentAsString

        MatcherAssert.assertThat(carString, Matchers.`is`(Matchers.equalTo(x)))
    }

    @Test
    fun FailedfindAllCarsNoPurchased() {
        var pageRamdom = (50..50000).random()
        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/open?page=$pageRamdom"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun findById() {
        val carFromService = carService.findAllCarsNoPurchased(0).first()

        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/open/${carFromService.id}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", Matchers.`is`(carFromService.id.toInt())))

    }

    @Test
    fun findByIdIsEmpty() {
        var idRamdom = (500..50000).random()
        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/open/$idRamdom"))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
                .andExpect(jsonPath("$").doesNotExist())

    }

    @Test
    fun login() {
        val customer = Customer(name = "Creditas", surname = "Creditas", identification = "12345A", birthday = LocalDate.of(2017, 1, 13), email = "creditas@creditas.com", password = "creditas")

        var bearer = mockMvc.perform(MockMvcRequestBuilders.post("$customerEndPoint/login")
                .body(data = customer, mapper = mapper))
                .andExpect(status().isOk)


    }

    @Test
    fun loginFail() {
        val customer = Customer(name = "Creditas", surname = "Creditas", identification = "12345A", birthday = LocalDate.of(2017, 1, 13), email = "pepito@creditas.com", password = "pepito")

        mockMvc.perform(MockMvcRequestBuilders.post("$customerEndPoint/login")
                .body(data = customer, mapper = mapper))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun saveSuccessfully() {
        val brand = brandService.findAllBrands().first()
        val model = modelService.findAllModelsByBrand(brand).first()
        val car = Car(number_plate = "2385 AYR", model = model, km = 50000, fuel_type = 2, price = 300000F, color = 9, year = 2010, url_image = "https://cdn.topgear.es/sites/navi.axelspringer.es/public/styles/1200/public/media/image/2017/05/prueba-ford-mustang-gt_19.jpg?itok=037w0axH")
        val bearer = doLogin()
        val carFromApi: Car = mockMvc.perform(MockMvcRequestBuilders.post("$carsEndPoint/save")
                .header("Authorization", bearer)
                .body(data = car, mapper = mapper)
        )
                .andExpect(status().isCreated)
                .bodyTo(mapper)
        println(carFromApi.number_plate)
        MatcherAssert.assertThat(carService.findCarById(carFromApi.id)?.number_plate, Matchers.`is`(carMustang.number_plate))
    }


}
