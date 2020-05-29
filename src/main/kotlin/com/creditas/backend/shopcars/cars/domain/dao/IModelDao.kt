package com.creditas.backend.shopcars.cars.domain.dao

import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Model
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IModelDao: JpaRepository<Model, Long> {
    fun findAllByBrand(brand : Brand): List<Model>
}