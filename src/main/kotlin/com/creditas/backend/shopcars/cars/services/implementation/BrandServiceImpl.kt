package com.creditas.backend.shopcars.cars.services.implementation

import com.creditas.backend.shopcars.cars.domain.dao.IBrandDao
import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.services.IBrandService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class BrandServiceImpl (val brandDao: IBrandDao): IBrandService {
    override fun findAllBrands(): List<Brand> = brandDao.findAll()

    override fun findBrandById(id: Long): Brand? = brandDao.findByIdOrNull(id)

    override fun saveBrand(brand: Brand): Brand = brandDao.save(brand)

    override fun updateBrand(brand: Brand): Brand {
        return if(brandDao.existsById(brand.id)) brandDao.save(brand)
        else throw  EntityNotFoundException("Brand id:${brand.id} does not exists")
    }

    override fun deleteBrandById(id: Long): Brand {
        return this.findBrandById(id)?.apply {
            brandDao.deleteById(this.id)
        } ?: throw EntityNotFoundException("Brand id:$id does not exists")  }
}