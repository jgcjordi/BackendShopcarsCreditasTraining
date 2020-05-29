package com.creditas.backend.shopcars.cars.controllers

import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.services.implementation.CarServiceImpl

import org.apache.juli.logging.LogFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("api/v1/cars")
class CarController (private val carServiceImpl: CarServiceImpl) {

    private val LOGGER = LogFactory.getLog("PhoneController.class")

    //http://localhost:8080/api/v1/cars
    @GetMapping
    fun findAllCars() = carServiceImpl.findAllCars()

    //http://localhost:8080/api/v1/cars/1
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Car> {
        val entity = carServiceImpl.findCarById(id)
        return ResponseEntity.status(
                if (entity != null) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }

    @PostMapping("/save")
    fun save(@RequestBody body: Car) = ResponseEntity.status(HttpStatus.CREATED)
            .body(carServiceImpl.saveCar(body))

    @PutMapping
    fun update(@RequestBody body: Car) = carServiceImpl.updateCar(body)

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) = carServiceImpl.deleteCarById(id)



    //http://localhost:8080/api/v1/cars/models
    @GetMapping("/{id}")
    fun findAllCars() = carServiceImpl.findAllCars()

}