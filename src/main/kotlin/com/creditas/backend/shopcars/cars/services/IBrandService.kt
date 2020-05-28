package com.creditas.backend.shopcars.cars.services

import com.creditas.backend.shopcars.cars.domain.entities.Brand

interface IBrandService {
    fun getBrands() :List<Brand>
    fun getBrandById(id:Long): Brand?
    fun addBrand(brand: Brand) : Brand
    fun deleteModel(model: Brand)
}