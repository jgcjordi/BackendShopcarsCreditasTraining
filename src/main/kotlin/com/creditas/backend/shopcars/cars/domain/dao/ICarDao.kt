package com.creditas.backend.shopcars.cars.domain.dao

import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.domain.entities.Model
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ICarDao: JpaRepository<Car, Long>{
    fun findByPurchaserIsNull(): List<Car>
    fun findByModelAndPurchaserIsNull(model:Model): List<Car>
}