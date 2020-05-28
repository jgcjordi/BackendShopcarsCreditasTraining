package com.creditas.backend.shopcars.cars.domain.dao

import com.creditas.backend.shopcars.cars.domain.entities.Car
import org.springframework.data.repository.CrudRepository

interface ICarDao: CrudRepository<Car, Long>{

}