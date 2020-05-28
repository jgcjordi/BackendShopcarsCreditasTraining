package com.creditas.backend.shopcars.cars.services

import com.creditas.backend.shopcars.cars.domain.dao.CarDao
import com.creditas.backend.shopcars.cars.domain.entities.Car
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class CarService(private val carDao: CarDao) : BasicCrud<Car, Long> {
    override fun findAll(): List<Car> = carDao.findAll()

    override fun findById(id: Long): Car? = carDao.findByIdOrNull(id)

    override fun save(t: Car): Car {
        return if (!carDao.existsById(t.id)) carDao.save(t) else throw DuplicateKeyException("Car id:${t.id} does exists")
    }

    override fun update(t: Car): Car {
        return if (carDao.existsById(t.id)) carDao.save(t) else throw  EntityNotFoundException("Car id:${t.id} does not exists")
    }

    override fun deleteById(id: Long): Car {
        return findById(id)?.apply {
            carDao.deleteById(this.id)
        } ?: throw EntityNotFoundException("Car id:$id does not exists")
    }
}