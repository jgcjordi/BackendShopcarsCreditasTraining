package com.creditas.backend.shopcars.cars.controllers

import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.domain.entities.Model
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
class CarController (
        private val carService: CarServiceImpl,
        private val brandsService: BrandServiceImpl,
        private val modelService: ModelServiceImpl) {

    private val LOGGER = LogFactory.getLog("CarController.class")

    //http://localhost:8080/api/v1/cars/open
    @GetMapping("/open")
    fun findAllCars() = carService.findAllCarsNoPurchased()


    //http://localhost:8080/api/v1/cars/1
    @GetMapping("/open/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Car> {
        val entity = carService.findCarById(id)
        return ResponseEntity.status(
                if (entity != null) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }

    @PostMapping("/save")
    fun save(@RequestBody car: Car): ResponseEntity<Car> {
        val model = modelService.findModelById(car.model.id)
        return if(model != null){
            car.model = model
            ResponseEntity.status(HttpStatus.CREATED)
                    .body(carService.saveCar(car))
        }else{
            car.model.name = "This model not found"
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(car)
        }
    }

    @PutMapping("/update")
    fun update(@RequestBody car: Car) = carService.updateCar(car)

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) = carService.deleteCarById(id)


    //http://localhost:8080/api/v1/cars/brands
    @GetMapping("/open/brands")
    fun findAllBrands() = brandsService.findAllBrands()

    //http://localhost:8080/api/v1/cars/models-of-brand
    @PostMapping("/open/models-of-brand")
    fun findAllModelsOfBrand(@RequestBody brand: Brand):ResponseEntity<List<Model>> {
        val entity = modelService.findAllModelsByBrand(brand)
        return ResponseEntity.status(
                if (entity.isNotEmpty()) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }



}