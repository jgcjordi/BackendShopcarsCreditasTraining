package com.creditas.backend.shopcars

import com.creditas.backend.shopcars.cars.domain.entities.Brand
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
class OnBoot(private val carService: CarService) : ApplicationRunner {
	override fun run(args: ApplicationArguments?) {
		carService.save(Brand(0, name="Ford"))
		println(carService.findAll()[0].name)
	}
}

@Service
class CarService(val carDAO: CarDao) {
	fun findAll(): List<Brand> = carDAO.findAll()
	fun save(t: Brand) = carDAO.save(t)
}

@Repository
interface CarDao : JpaRepository<Brand, Int>

/*
@Entity
data class Car(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		var id: Long = 0,
		val name: String
)*/

//Prueba1


