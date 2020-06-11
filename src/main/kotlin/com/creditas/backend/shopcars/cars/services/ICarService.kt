package com.creditas.backend.shopcars.cars.services

import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.domain.entities.Model
import org.springframework.data.domain.Page

interface ICarService {
    fun findAllCars(page:Int): Page<Car>
    fun findCarById(id:Long):Car?
    fun saveCar(car: Car): Car
    fun updateCar(car: Car): Car
    fun deleteCarById(id:Long): Car
}