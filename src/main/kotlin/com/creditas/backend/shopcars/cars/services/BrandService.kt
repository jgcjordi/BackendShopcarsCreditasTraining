package com.creditas.backend.shopcars.cars.services

import com.creditas.backend.shopcars.cars.domain.dao.BrandDao
import com.creditas.backend.shopcars.cars.domain.entities.Brand
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class BrandService(private val brandDao: BrandDao) : BasicCrud<Brand, Long> {
    override fun findAll(): List<Brand> = brandDao.findAll()

    override fun findById(id: Long): Brand? = brandDao.findByIdOrNull(id)

    override fun save(t: Brand): Brand {
        return if (!brandDao.existsById(t.id)) brandDao.save(t) else throw DuplicateKeyException("Brand id:${t.id} does exists")
    }

    override fun update(t: Brand): Brand {
        return if (brandDao.existsById(t.id)) brandDao.save(t) else throw  EntityNotFoundException("Brand id:${t.id} does not exists")
    }

    override fun deleteById(id: Long): Brand {
        return findById(id)?.apply {
            brandDao.deleteById(this.id)
        } ?: throw EntityNotFoundException("Brand id:$id does not exists")
    }
}