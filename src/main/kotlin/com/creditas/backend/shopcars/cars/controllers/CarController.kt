package com.creditas.backend.shopcars.cars.controllers

import com.creditas.backend.shopcars.application.infraestructure.controller.CustomerController
import com.creditas.backend.shopcars.application.services.implementation.CustomerServiceImpl
import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.domain.entities.Model
import com.creditas.backend.shopcars.cars.services.implementation.BrandServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.CarServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.ModelServiceImpl
import org.apache.juli.logging.LogFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@CrossOrigin
@RequestMapping("api/v1/cars")
class CarController (
        private val carService: CarServiceImpl,
        private val brandsService: BrandServiceImpl,
        private val modelService: ModelServiceImpl,
        private val customerService: CustomerServiceImpl,
        private val customerController: CustomerController){
    private val LOGGER = LogFactory.getLog("CarController.class")

    //http://localhost:8080/api/v1/cars/open
    @GetMapping("/open")
    fun findAllCarsNoPurchased(@RequestParam(defaultValue = "0")  page:Int) : ResponseEntity<Page<Car>> {
        val result = carService.findAllCarsNoPurchased(page)
        return ResponseEntity.status(
                if(result.content.size!=0) HttpStatus.OK else HttpStatus.BAD_REQUEST).body(result)
    }

    //http://localhost:8080/api/v1/cars/1
    @GetMapping("/open/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Car> {
        val entity = carService.findCarById(id)
        return ResponseEntity.status(
                if (entity != null) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }

    @PostMapping("/save")
    fun save(request: HttpServletRequest, @RequestBody car: Car): ResponseEntity<Car> {
        return try{
            val saveCar: Car = carService.saveCar(car)
            customerController.getCustomerByToken(request)?.apply {
                this.seller_car.add(saveCar)
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(carService.saveCar(car))
        }
        catch (e:Exception){
            ResponseEntity.status(HttpStatus.CONFLICT).body(null)
        }
    }

    @PutMapping("/update")
    fun update(@RequestBody car: Car) = carService.updateCar(car)

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long, request: HttpServletRequest):ResponseEntity<String> {
            carService.findCarById(id)?.apply {
                val carToDelete = this
                customerController.getCustomerByToken(request)?.apply() {
                    this.seller_car.remove(carToDelete)
                    customerService.updateCustomer(this)
                    carService.deleteCarById(carToDelete.id)
                    return ResponseEntity.status(HttpStatus.OK).body("Coche eliminado del usuario y de la bd")
                }
                return ResponseEntity.status(HttpStatus.OK).body("Fallo al eliminar el coche")
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("El id del coche no existe")
    }


    //http://localhost:8080/api/v1/cars/open/brands
    @GetMapping("/open/brands")
    fun findAllBrands() = brandsService.findAllBrands()

    //http://localhost:8080/api/v1/cars/open/models-of-brand
    @PostMapping("/open/models-of-brand")
    fun findAllModelsOfBrand(@RequestBody brand: Brand):ResponseEntity<List<Model>> {
        val entity = modelService.findAllModelsByBrand(brand)
        return ResponseEntity.status(
                if (entity.isNotEmpty()) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }

    //http://localhost:8080/api/v1/open/cars-of-model
    @PostMapping("/open/cars-of-model")
    fun findAllCarsNoPurchasedOfModel(@RequestBody model: Model, @RequestParam(defaultValue = "0") page: Int):ResponseEntity<Page<Car>> {
        val result = carService.findAllCarsNoPurchasedOfModel(model, page)
        return ResponseEntity(result, HttpStatus.OK)
    }


    //http://localhost:8080/api/v1/open/cars-of-brand
    @PostMapping("/open/cars-of-brand")
    fun findAllCarsNoPurchasedOfBrand(@RequestBody brand: Brand,  @RequestParam(defaultValue = "0") page: Int):ResponseEntity<Page<Car>> {
        val result = carService.findAllCarsNoPurchasedOfBrand(brand, page)
        return ResponseEntity(result, HttpStatus.OK)
    }


    @GetMapping("purcharse/{idCar}")
    fun purcharseCar(request: HttpServletRequest, @PathVariable idCar: Long): ResponseEntity<String> {
        carService.findCarById(idCar)?.apply {
            val carPurcharse = this
            customerController.getCustomerByToken(request)?.apply() {
                this.purchaser_car.add(carPurcharse)
                customerService.updateCustomer(this)
                return ResponseEntity.status(HttpStatus.OK).body("Coche comprado correctamente")
            }
            return ResponseEntity.status(HttpStatus.OK).body("Fallo en la compra del coche")
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("El id del coche no existe")
    }

    @GetMapping("/findPurcharseCars")
    fun findPurcharseCars(request: HttpServletRequest): ResponseEntity<List<Car>> {
         var entity: List<Car> = mutableListOf()
            customerController.getCustomerByToken(request)?.apply() {
                entity = carService.findPurcharseCars(this.id)
            }
        return ResponseEntity.status( if (entity.isNotEmpty()) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }

    @GetMapping("/findSellerCars")
    fun findSellerCars(request: HttpServletRequest): ResponseEntity<List<Car>> {
        var entity: List<Car> = mutableListOf()
        customerController.getCustomerByToken(request)?.apply() {
            entity = carService.findSellerCars(this.id)
        }
        return ResponseEntity.status( if (entity.isNotEmpty()) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }

    //http://localhost:8080/api/v1/findLastPurcharseCar
    @GetMapping("/findLastPurcharseCar")
    fun findLastPurcharseCar(request: HttpServletRequest): ResponseEntity<Car> {
        var entity: List<Car> = mutableListOf()
        customerController.getCustomerByToken(request)?.apply() {
            entity = carService.findPurcharseCars(this.id)
        }
        return ResponseEntity.status( if (entity.isNotEmpty()) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity[entity.size-1])
    }

}