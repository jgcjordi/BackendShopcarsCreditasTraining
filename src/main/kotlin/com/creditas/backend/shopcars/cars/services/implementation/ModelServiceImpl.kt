package com.creditas.backend.shopcars.cars.services.implementation

import com.creditas.backend.shopcars.cars.domain.dao.IModelDao
import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Model
import com.creditas.backend.shopcars.cars.services.IModelService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ModelServiceImpl(val modelDao: IModelDao): IModelService {
    override fun getModels(): List<Model> = modelDao.findAll() as List<Model>

    override fun getModelById(id: Long): Model? = modelDao.findByIdOrNull(id)

    override fun getModelsByBrand(brand: Brand): List<Model> = modelDao.findAllByBrand(brand)

    override fun addModel(model: Model): Model = modelDao.save(model)

    override fun deleteModel(model: Model) {
        modelDao.delete(model)
    }
}