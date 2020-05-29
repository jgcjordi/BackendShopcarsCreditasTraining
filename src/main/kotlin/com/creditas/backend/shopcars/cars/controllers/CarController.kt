package com.creditas.backend.shopcars.cars.controllers

import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.services.implementation.BrandServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.CarServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.ModelServiceImpl

import org.apache.juli.logging.LogFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("api/v1/cars")
class CarController (private val carService: CarServiceImpl, private val brandsService: BrandServiceImpl) {

    private val LOGGER = LogFactory.getLog("CarController.class")

    //http://localhost:8080/api/v1/cars
    @GetMapping
    fun findAllCars() = carService.findAllCars()

    //http://localhost:8080/api/v1/cars/1
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Car> {
        val entity = carService.findCarById(id)
        return ResponseEntity.status(
                if (entity != null) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }

    @PostMapping("/save")
    fun save(@RequestBody body: Car) = ResponseEntity.status(HttpStatus.CREATED)
            .body(carService.saveCar(body))

    @PutMapping
    fun update(@RequestBody body: Car) = carService.updateCar(body)

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) = carService.deleteCarById(id)



    //http://localhost:8080/api/v1/cars/brands
    @GetMapping("/brands")
    fun findAllBrands() = brandsService.findAllBrands()


}