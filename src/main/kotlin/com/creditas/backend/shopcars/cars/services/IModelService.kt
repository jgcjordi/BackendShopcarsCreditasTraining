package com.creditas.backend.shopcars.cars.services

import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Model

interface IModelService {
    fun getModels() :List<Model>
    fun getModelById(id: Long): Model?
    fun getModelsByBrand(brand: Brand): List<Model>
    fun addModel(model: Model) :Model
    fun deleteModel(model:Model)
}