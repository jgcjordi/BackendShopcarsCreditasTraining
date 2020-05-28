package com.creditas.backend.shopcars.cars.services.implementation

import com.creditas.backend.shopcars.cars.domain.dao.IModelDao
import com.creditas.backend.shopcars.cars.domain.entities.Model
import com.creditas.backend.shopcars.cars.services.IModelService
import java.util.*

class ModelServiceImpl(val modelDao: IModelDao): IModelService {
    override fun getModels(): List<Model> = modelDao.findAll() as List<Model>

    override fun addModel(model: Model): Model = modelDao.save(model)

    override fun deleteModel(model: Model) {
        modelDao.delete(model)
    }
}