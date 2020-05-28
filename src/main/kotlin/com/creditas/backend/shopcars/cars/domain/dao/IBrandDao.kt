package com.creditas.backend.shopcars.cars.domain.dao

import com.creditas.backend.shopcars.cars.domain.entities.Brand
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IBrandDao: CrudRepository<Brand, Long> {
}