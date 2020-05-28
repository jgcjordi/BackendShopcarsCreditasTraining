package com.creditas.backend.shopcars.cars.services

import com.creditas.backend.shopcars.cars.domain.entities.Car

interface ICarService {
    fun getCars(): List<Car>
    fun getCarbyId8(id:Long):Car?
    fun addCar(car: Car): Car
    fun deleteCar(car:Car): Car
}