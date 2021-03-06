package com.creditas.backend.shopcars

import com.creditas.backend.shopcars.application.domain.entities.Customer
import com.creditas.backend.shopcars.application.services.implementation.CustomerServiceImpl
import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.services.implementation.BrandServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.CarServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.ModelServiceImpl
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDate


@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
class ShopCarsApplicationTests {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var carService: CarServiceImpl

    @Autowired
    private lateinit var customerService: CustomerServiceImpl

    @Autowired
    private lateinit var modelService: ModelServiceImpl

    @Autowired
    private lateinit var brandService: BrandServiceImpl

    private val carsEndPoint = "/api/v1/cars"
    private val customerEndPoint = "/api/v1/customers"

    private val mockMvc: MockMvc by lazy {//inicializacion perezosa, sera inicializada cuando se necesitada, bajo demanda "by".
        //configura el mockMvc para que cada vez que haga una peticion http la imprima en la consola.
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply<DefaultMockMvcBuilder>(springSecurity())
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
        val customer = Customer(name = "Admin", surname = "Admin", identification = "1234A", birthday = LocalDate.of(2017, 1, 13), email = "admin@admin.com", password = "admin")

        return mockMvc.perform(MockMvcRequestBuilders.post("$customerEndPoint/login")
                .body(data = customer, mapper = mapper))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
    }
    fun doLoginCustomerEmptyPurchaseAndSellers(): String {
        val customer = Customer (name = "Creditas1", surname = "Creditas1", identification = "123456A", birthday = LocalDate.of(2017, 1, 13), email = "creditas1@creditas.com", password = "creditas1")

        return mockMvc.perform(MockMvcRequestBuilders.post("$customerEndPoint/login")
                .body(data = customer, mapper = mapper))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
    }

    @Test
    fun findAllCarsNoPurchased() {
        val page = 1
        val carsFromService: List<Car> = carService.findAllCarsNoPurchased(page).content
        val carsString: String = mapper.writeValueAsString(carsFromService)
        val carsListFromService = mapper.readValue(carsString, object : TypeReference<List<Car>>() {}) as List<Car>

        val jsonString = mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/open?page=$page"))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString

        val jsonCarList: JsonNode = mapper.readTree(jsonString).path("content")
        val resultCarList = mapper.readValue(jsonCarList.toString(), object : TypeReference<List<Car>>() {}) as List<Car>

        MatcherAssert.assertThat(carsListFromService, Matchers.`is`(Matchers.equalTo(resultCarList)))
    }

    @Test
    fun failedFindAllCarsNoPurchased() {
        val pageRandom = (50..50000).random()
        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/open?page=$pageRandom"))
                .andExpect(status().isBadRequest)
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
        val idRandom = (500..50000).random()
        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/open/$idRandom"))
                .andExpect(status().isNoContent)
                .andExpect(jsonPath("$").doesNotExist())

    }

    @Test
    fun login() {
        val customer = Customer(name = "Creditas", surname = "Creditas", identification = "12345A", birthday = LocalDate.of(2017, 1, 13), email = "creditas@creditas.com", password = "creditas")

        mockMvc.perform(MockMvcRequestBuilders.post("$customerEndPoint/login")
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
        val car = Car(number_plate = "6589 AYR", model = model, km = 50000, fuel_type = 2, price = 300000F, color = 9, year = 2010, url_image = "https://cdn.topgear.es/sites/navi.axelspringer.es/public/styles/1200/public/media/image/2017/05/prueba-ford-mustang-gt_19.jpg?itok=037w0axH")
        val bearer = doLogin()
        val carFromApi: Car = mockMvc.perform(MockMvcRequestBuilders.post("$carsEndPoint/save")
                .header("Authorization", bearer)
                .body(data = car, mapper = mapper)
        )
                .andExpect(status().isCreated)
                .bodyTo(mapper)

        MatcherAssert.assertThat(carService.findCarById(carFromApi.id)?.number_plate, Matchers.`is`(car.number_plate))
    }

    @Test
    fun saveDuplicateEntity() {
        val carsFromService = carService.findAllCarsNoPurchased(0).content
        assert(carsFromService.isNotEmpty()) { "Should not be empty" }
        val car = carsFromService.first()
        val bearer = doLogin()
        mockMvc.perform(MockMvcRequestBuilders.post("$carsEndPoint/save")
                .header("Authorization", bearer)
                .body(car, mapper))
                .andExpect(status().isConflict)
        //.andExpect(jsonPath("$.title", Matchers.`is`("DuplicateKeyException")))

    }

    @Test
    fun deleteById() {
        val carFromService = carService.findAllCarsNoPurchased(1).first()
        val bearer = doLogin()
        mockMvc.perform(MockMvcRequestBuilders.delete("$carsEndPoint/${carFromService.id}")
                .header("Authorization", bearer))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", Matchers.`is`("Coche eliminado del usuario y de la bd")))

        assert((carService.findCarById(carFromService.id) == null))
    }

    @Test
    fun deleteByIdFail() {
        val idRandom = (500..50000).random()
        val bearer = doLogin()
        mockMvc.perform(MockMvcRequestBuilders.delete("$carsEndPoint/${idRandom}")
                .header("Authorization", bearer))
                .andExpect(status().isNoContent)
                .andExpect(jsonPath("$", Matchers.`is`("El id del coche no existe")))

    }

    @Test
    fun purchaseCar() {
        val carFromService = carService.findAllCarsNoPurchased(2).first()
        val bearer = doLogin()
        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/purcharse/${carFromService.id}")
                .header("Authorization", bearer))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", Matchers.`is`("Coche comprado correctamente")))

        /*   val x= mapper.writeValueAsString(carService.findCarById(carFromService.id)!!.purchaser.size).toInt()
           assert(x!=0)*/
    }

    @Test
    fun purchaseCarFail() {
        val idRandom = (500..50000).random()
        val bearer = doLogin()
        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/purcharse/${idRandom}")
                .header("Authorization", bearer))
                .andExpect(status().isNoContent)
                .andExpect(jsonPath("$", Matchers.`is`("El id del coche no existe")))

    }

    @Test
    fun findPurchaseCars() {
        val bearer = doLogin()
        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/findPurcharseCars")
                .header("Authorization", bearer))
                .andExpect(status().isOk)
    }

    @Test
    fun findPurchaseCarsFail() {
        val bearer = doLoginCustomerEmptyPurchaseAndSellers()
        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/findPurcharseCars")
                .header("Authorization", bearer))
                .andExpect(status().isNoContent)

    }

    @Test
    fun findSellerCars() {
        val bearer = doLogin()
        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/findSellerCars")
                .header("Authorization", bearer))
                .andExpect(status().isOk)
    }
    @Test
    fun findSellerCarsFail() {
        val bearer = doLoginCustomerEmptyPurchaseAndSellers()
        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/findSellerCars")
                .header("Authorization", bearer))
                .andExpect(status().isNoContent)
    }

    @Test
    fun findLastPurchaseCar() {
        val bearer = doLogin()
        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/findLastPurcharseCar")
                .header("Authorization", bearer))
                .andExpect(status().isOk)
    }
    @Test
    fun findLastPurchaseCarFail() {
        val bearer = doLoginCustomerEmptyPurchaseAndSellers()
        mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/findLastPurcharseCar")
                .header("Authorization", bearer))
                .andExpect(status().isNoContent)
    }

    @Test
    fun findAllBrands() {
        val brandsFromService = brandService.findAllBrands()

        val brandsString: String = mapper.writeValueAsString(brandsFromService)

        val  jsonString = mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/open/brands"))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
        val resultBrandList = mapper.readValue(jsonString, object : TypeReference<List<Brand>>() {}) as List<Brand>
        val resultBrandListFromService = mapper.readValue(brandsString, object : TypeReference<List<Brand>>() {}) as List<Brand>
        MatcherAssert.assertThat(resultBrandListFromService, Matchers.`is`(Matchers.equalTo(resultBrandList)))
    }

    @Test
    fun findAllCarsNoPurchasedOfModel() {
        val page = 0
        val brand = brandService.findAllBrands().first()
        val model = modelService.findAllModelsByBrand(brand).first()
        val cars = carService.findAllCarsNoPurchasedOfModel(model, page)
        val carsString: String = mapper.writeValueAsString(cars)

        val jsonString = mockMvc.perform(MockMvcRequestBuilders.post("$carsEndPoint/open/cars-of-model?page=$page")
                .body(model, mapper))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString

        MatcherAssert.assertThat(carsString, Matchers.`is`(Matchers.equalTo(jsonString)))
    }

    @Test
    fun findAllCarsNoPurchasedOfBrand() {
        val page = 0
        val brand = brandService.findAllBrands().first()
        val cars = carService.findAllCarsNoPurchasedOfBrand(brand, page)
        val carsString: String = mapper.writeValueAsString(cars)

        val jsonString = mockMvc.perform(MockMvcRequestBuilders.post("$carsEndPoint/open/cars-of-brand?page=$page")
                .body(brand, mapper))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString

        MatcherAssert.assertThat(carsString, Matchers.`is`(Matchers.equalTo(jsonString)))
    }

    @Test
    fun findAllModelsOfBrand(){
        val brand = brandService.findAllBrands().first()
        val modelsFromService = modelService.findAllModelsByBrand(brand)
        val modelsString = mapper.writeValueAsString(modelsFromService)
        val jsonString =  mockMvc.perform(MockMvcRequestBuilders.post("$carsEndPoint/open/models-of-brand")
                .body(brand,mapper))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString


        MatcherAssert.assertThat(modelsString, Matchers.`is`(Matchers.equalTo(jsonString)))
    }

    @Test
    fun findAllUsers(){
        val bearer = doLogin()

        mockMvc.perform(MockMvcRequestBuilders.get(customerEndPoint)
                .header("Authorization", bearer))
                .andExpect(status().isOk)
    }

    @Test
    fun findCustomerById(){
        val bearer = doLogin()
        val customerFromService = customerService.getCustomers().first()

        mockMvc.perform(MockMvcRequestBuilders.get("$customerEndPoint/${customerFromService.id}")
                .header("Authorization", bearer))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", Matchers.`is`(customerFromService.id.toInt())))
    }
    @Test
    fun findCustomerByIdFail(){
        val bearer = doLogin()
        val idRandom = (500..50000).random()

        mockMvc.perform(MockMvcRequestBuilders.get("$customerEndPoint/$idRandom")
                .header("Authorization", bearer))
                .andExpect(status().isNoContent)
    }

}
