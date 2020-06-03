package com.creditas.backend.shopcars.cars.services.implementation

import com.creditas.backend.shopcars.cars.domain.dao.ICarDao
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.domain.entities.Model
import com.creditas.backend.shopcars.cars.services.ICarService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class CarServiceImpl(private val carDao: ICarDao): ICarService{
    override fun findAllCars(): List<Car> = carDao.findAll()

    override fun findCarById(id: Long): Car? = carDao.findByIdOrNull(id)

    override fun saveCar(car: Car): Car = carDao.save(car)

    override fun updateCar(car: Car): Car{
        return if(carDao.existsById(car.id)) carDao.save(car)
        else throw  EntityNotFoundException("Car id:${car.id} does not exists")
    }

    override fun deleteCarById(id: Long): Car {
        return this.findCarById(id)?.apply {
            carDao.deleteById(this.id)
        } ?: throw EntityNotFoundException("Car id:$id does not exists")  }

    fun findAllCarsNoPurchased(): List<Car> = carDao.findByPurchaserIsNull()

    fun findAllCarsNoPurchasedOfModel(model: Model): List<Car> = carDao.findByModelAndPurchaserIsNull(model)
}