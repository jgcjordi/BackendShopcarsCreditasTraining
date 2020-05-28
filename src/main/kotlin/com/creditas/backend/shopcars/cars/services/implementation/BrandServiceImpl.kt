package com.creditas.backend.shopcars.cars.services.implementation

import com.creditas.backend.shopcars.cars.domain.dao.IBrandDao
import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.services.IBrandService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BrandServiceImpl (val brandDao: IBrandDao): IBrandService {
    override fun getBrands(): List<Brand> = brandDao.findAll() as List<Brand>

    override fun getBrandById(id: Long): Brand? = brandDao.findByIdOrNull(id)

    override fun addBrand(brand: Brand): Brand = brandDao.save(brand)

    override fun deleteModel(brand: Brand) = brandDao.delete(brand)
}