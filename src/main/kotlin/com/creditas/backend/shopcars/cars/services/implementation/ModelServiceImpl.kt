package com.creditas.backend.shopcars.cars.services.implementation

import com.creditas.backend.shopcars.cars.domain.dao.IModelDao
import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.domain.entities.Model
import com.creditas.backend.shopcars.cars.services.IModelService
import com.sun.org.apache.xpath.internal.operations.Mod
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class ModelServiceImpl(private val modelDao: IModelDao): IModelService {
    override fun findAllModels(): List<Model> = modelDao.findAll()

    override fun findModelById(id: Long): Model? = modelDao.findByIdOrNull(id)

    override fun saveModel(model: Model): Model = modelDao.save(model)

    override fun updateModel(model: Model): Model {
        return if(modelDao.existsById(model.id)) modelDao.save(model)
        else throw  EntityNotFoundException("Model id:${model.id} does not exists")
    }

    override fun deleteModelById(id: Long): Model {
        return this.findModelById(id)?.apply {
            modelDao.deleteById(this.id)
        } ?: throw EntityNotFoundException("Model id:$id does not exists")  }



    override fun findAllModelsByBrand(brand: Brand): List<Model> = modelDao.findAllByBrand(brand)
}