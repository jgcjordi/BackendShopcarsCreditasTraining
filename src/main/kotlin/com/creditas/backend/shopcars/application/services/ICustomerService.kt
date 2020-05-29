package com.creditas.backend.shopcars.application.services

import com.creditas.backend.shopcars.application.domain.entities.Customer
import javax.servlet.http.HttpServletRequest

interface ICustomerService {
    fun getCustomers(): List<Customer>
    fun findCustomerByID(idCustomer:Long): Customer?
    fun saveCustomer(customer:Customer): Customer
    fun updateCustomer(customer: Customer): Customer
    fun findCustomerByEmail(email:String): Customer?
    fun deleteCustomerById(idCustomer:Long)
    fun getJWT(customer: Customer, request: HttpServletRequest):String
    fun login(email:String,password:String):Customer
}