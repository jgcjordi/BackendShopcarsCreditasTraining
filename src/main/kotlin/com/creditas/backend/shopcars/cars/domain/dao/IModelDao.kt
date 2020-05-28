package com.creditas.backend.shopcars.cars.domain.dao

import com.creditas.backend.shopcars.cars.domain.entities.Model
import org.springframework.data.repository.CrudRepository

interface IModelDao: CrudRepository<Model, Long> {
}