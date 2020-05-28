package com.creditas.backend.shopcars

import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.domain.entities.Model
import com.creditas.backend.shopcars.cars.services.BrandService
import com.creditas.backend.shopcars.cars.services.CarService
import com.creditas.backend.shopcars.cars.services.ModelService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component


@SpringBootApplication
class ShopCarsApplication

fun main(args: Array<String>) {
	runApplication<ShopCarsApplication>(*args)
}

@Component
class OnBoot(
		private val carService: CarService,
		private val modelService: ModelService,
		private val brandService: BrandService) : ApplicationRunner {

	override fun run(args: ApplicationArguments?) {
		val ford = brandService.save(Brand(name="Ford"))
		val mustang = modelService.save(Model(name="Mustang", brand = ford))
		val fiesta = modelService.save(Model(name="Fiesta", brand = ford))
		carService.save(Car(numberPlate = "2385 AYR", model = mustang))
		carService.save(Car(numberPlate = "4577 JGC", model = fiesta))

		println(carService.findAll()[0].numberPlate)
	}
}