package com.creditas.backend.shopcars.cars.domain.dao

import com.creditas.backend.shopcars.cars.domain.entities.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IBrandDao: JpaRepository<Brand, Long>