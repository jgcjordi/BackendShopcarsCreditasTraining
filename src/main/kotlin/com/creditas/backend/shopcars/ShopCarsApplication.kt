package com.creditas.backend.shopcars

import com.creditas.backend.shopcars.application.domain.entities.Customer
import com.creditas.backend.shopcars.application.services.implementation.customerServiceImpl
import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.domain.entities.Model
import com.creditas.backend.shopcars.cars.services.implementation.BrandServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.CarServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.ModelServiceImpl
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import java.time.LocalDate

@SpringBootApplication
class ShopCarsApplication

fun main(args: Array<String>) {
	runApplication<ShopCarsApplication>(*args)
}

@Component
class OnBoot(
		private val carService: CarServiceImpl,
		private val modelService: ModelServiceImpl,
		private val brandService: BrandServiceImpl,
		private val customerService: customerServiceImpl) : ApplicationRunner {

	override fun run(args: ApplicationArguments?) {
		val user1 = customerService.saveCustomer(Customer(name="Admin", surname= "Admin",birthday =  LocalDate.of(2017, 1, 13),email="admin@admin.com",password="admin"))
		val ford = brandService.saveBrand(Brand(name="Ford"))
		val mustang = modelService.saveModel(Model(name="Mustang", brand = ford))
		val fiesta = modelService.saveModel(Model(name="Fiesta", brand = ford))
		carService.saveCar(Car(number_plate = "2385 AYR", model = mustang))
		carService.saveCar(Car(number_plate = "4577 JGC", model = fiesta))

		println(carService.findAllCars()[0].number_plate)
	}
}

