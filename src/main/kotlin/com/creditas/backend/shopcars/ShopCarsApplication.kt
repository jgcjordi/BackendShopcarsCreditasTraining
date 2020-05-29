package com.creditas.backend.shopcars

import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Model
import com.creditas.backend.shopcars.cars.services.implementation.BrandServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.ModelServiceImpl
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
class OnBoot(private val brandService: BrandServiceImpl, private val modelService: ModelServiceImpl) : ApplicationRunner {
	override fun run(args: ApplicationArguments?) {
		val brand: Brand = brandService.addBrand(Brand(0, name="Ford"))
		modelService.saveCar(Model(0,"Fiesta",brand))
		println(brandService.getBrands()[0].name)
		println(modelService.findAllModels()[0].brand!!.name)
	}
}

//Prueba1


