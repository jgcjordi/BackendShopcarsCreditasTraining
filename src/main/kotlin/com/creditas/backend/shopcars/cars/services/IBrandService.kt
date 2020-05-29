package com.creditas.backend.shopcars.cars.services

import com.creditas.backend.shopcars.cars.domain.entities.Brand

interface IBrandService {
    fun findAllBrands() :List<Brand>
    fun findBrandById(id: Long): Brand?
    fun saveBrand(brand: Brand) : Brand
    fun updateBrand(brand: Brand) : Brand
    fun deleteBrandById(id: Long): Brand
}