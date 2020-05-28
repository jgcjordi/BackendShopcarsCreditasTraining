package com.creditas.backend.shopcars.cars.services.implementation

import com.creditas.backend.shopcars.cars.domain.dao.ICarDao
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.services.ICarService
import org.springframework.stereotype.Service

@Service
class CarServiceImpl(var ICarDao: ICarDao): ICarService{
    override fun getCars(): List<Car> = getCars()

    override fun getCarbyId8(id: Long): Car? = getCarbyId8(id)

    override fun addCar(car: Car): Car = addCar(car)

    override fun deleteCar(car: Car): Car = deleteCar(car)

}