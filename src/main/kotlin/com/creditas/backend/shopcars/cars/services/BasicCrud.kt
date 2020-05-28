package com.creditas.backend.shopcars.cars.services

interface BasicCrud<T, ID> {
fun findAll(): List<T>
    fun findById(id: ID): T?
    fun save(t: T): T
    fun update(t: T): T
    fun deleteById(id: ID): T
}