package com.creditas.backend.shopcars.cars.services

import com.creditas.backend.shopcars.cars.domain.dao.ModelDao
import com.creditas.backend.shopcars.cars.domain.entities.Model
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class ModelService(private val modelDao: ModelDao) : BasicCrud<Model, Long> {
    override fun findAll(): List<Model> = modelDao.findAll()

    override fun findById(id: Long): Model? = modelDao.findByIdOrNull(id)

    override fun save(t: Model): Model {
        return if (!modelDao.existsById(t.id)) modelDao.save(t) else throw DuplicateKeyException("Model id:${t.id} does exists")
    }

    override fun update(t: Model): Model {
        return if (modelDao.existsById(t.id)) modelDao.save(t) else throw  EntityNotFoundException("Model id:${t.id} does not exists")
    }

    override fun deleteById(id: Long): Model {
        return findById(id)?.apply {
            modelDao.deleteById(this.id)
        } ?: throw EntityNotFoundException("Model id:$id does not exists")
    }
}