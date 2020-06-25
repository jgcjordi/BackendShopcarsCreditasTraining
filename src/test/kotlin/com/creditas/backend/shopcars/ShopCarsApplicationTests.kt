package com.creditas.backend.shopcars

import com.creditas.backend.shopcars.cars.services.implementation.CarServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
class ShopCarsApplicationTests {

	@Autowired
	private lateinit var webApplicationContext: WebApplicationContext

	@Autowired
	private lateinit var mapper: ObjectMapper

	@Autowired
	private lateinit var carService: CarServiceImpl

	private val carsEndPoint = "/api/v1/cars"

	private val mockMvc: MockMvc by lazy {//inicializacion perezosa, sera inicializada cuando se necesitada, bajo demanda "by".
		//configura el mockMvc para que cada vez que haga una peticion http la imprima en la consola.
		MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
	}

	//para poder aceptar parametros genericos tiene que ser una funcion inline
	private inline fun <reified T> ResultActions.bodyTo(mapper: ObjectMapper = jacksonObjectMapper()):T{
		println(this)
		return mapper.readValue(this.andReturn().response.contentAsByteArray)
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
		var pageRamdom= (50..50000).random()
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
		var idRamdom= (500..50000).random()
		mockMvc.perform(MockMvcRequestBuilders.get("$carsEndPoint/open/$idRamdom"))
				.andExpect(MockMvcResultMatchers.status().isNoContent)
				.andExpect(jsonPath("$").doesNotExist())

	}

}
