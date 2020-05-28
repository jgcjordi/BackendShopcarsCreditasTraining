package com.creditas.backend.shopcars.cars.domain.dao

import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Model
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IModelDao: CrudRepository<Model, Long> {
    fun findAllByBrand(brand : Brand): List<Model>
}