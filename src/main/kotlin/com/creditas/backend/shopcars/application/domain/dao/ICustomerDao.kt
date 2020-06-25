package com.creditas.backend.shopcars.application.domain.dao

import com.creditas.backend.shopcars.application.domain.entities.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ICustomerDao: JpaRepository<Customer, Long>{
    fun findOneByEmail(email: String): Customer?
}