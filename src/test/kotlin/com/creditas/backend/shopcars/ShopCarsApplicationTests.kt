package com.creditas.backend.shopcars

import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.services.implementation.CarServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.annotation.RequestParam
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

	fun findAllCarsNoPurchased(@RequestParam(defaultValue = "0")  page:Int) : ResponseEntity<Page<Car>> {
		val result = carService.findAllCarsNoPurchased(page)
		return ResponseEntity(result, HttpStatus.OK)
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

		println("el string:$x")
		println("el car string:$carString")

		MatcherAssert.assertThat(carString, Matchers.`is`(Matchers.equalTo(x)))
	}

}
