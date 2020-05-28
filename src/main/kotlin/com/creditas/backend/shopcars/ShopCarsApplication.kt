package com.creditas.backend.shopcars

import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Model
import com.creditas.backend.shopcars.cars.services.implementation.BrandServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.ModelServiceImpl
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@SpringBootApplication
class ShopCarsApplication

fun main(args: Array<String>) {
	runApplication<ShopCarsApplication>(*args)
}

@Component
class OnBoot(private val brandService: BrandServiceImpl, private val modelService: ModelServiceImpl) : ApplicationRunner {
	override fun run(args: ApplicationArguments?) {
		val brand: Brand = brandService.addBrand(Brand(0, name="Ford"))
		modelService.addModel(Model(0,"Fiesta",brand))
		println(brandService.getBrands()[0].name)
		println(modelService.getModels()[0].brand!!.name)
	}
}

//Prueba1


