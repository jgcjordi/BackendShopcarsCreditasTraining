package com.creditas.backend.shopcars.cars.services.implementation

import com.creditas.backend.shopcars.cars.domain.dao.ICarDao
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.services.ICarService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class CarServiceImpl(private val ICarDao: ICarDao): ICarService{
    override fun findAllCars(): List<Car> = ICarDao.findAll()

    override fun findCarById(id: Long): Car? = ICarDao.findByIdOrNull(id)

    override fun saveCar(car: Car): Car = ICarDao.save(car)

    override fun updateCar(car: Car): Car{
        return if(ICarDao.existsById(car.id)) ICarDao.save(car)
        else throw  EntityNotFoundException("Car id:${car.id} does not exists")
    }

    override fun deleteCarById(id: Long): Car {
        return this.findCarById(id)?.apply {
            ICarDao.deleteById(this.id)
        } ?: throw EntityNotFoundException("Car id:$id does not exists")  }

}