package com.creditas.backend.shopcars.cars.services

import com.creditas.backend.shopcars.cars.domain.entities.Car

interface ICarService {
    fun findAllCars(): List<Car>
    fun findCarById(id:Long):Car?
    fun saveCar(car: Car): Car
    fun updateCar(car: Car): Car
    fun deleteCarById(id:Long): Car
}