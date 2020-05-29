package com.creditas.backend.shopcars.cars.services

import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Model

interface IModelService {
    fun findAllModels() :List<Model>
    fun findModelById(id: Long): Model?
    fun saveModel(model: Model) :Model
    fun updateModel(model: Model) :Model
    fun deleteModelById(id: Long):Model

    fun findAllModelsByBrand(brand: Brand): List<Model>

}