package com.creditas.backend.shopcars.application.services.implementation

import com.creditas.backend.shopcars.application.domain.dao.ICustomerDao
import com.creditas.backend.shopcars.application.domain.entities.Customer
import com.creditas.backend.shopcars.application.infraestructure.security.JWTAuthorizationFilter
import com.creditas.backend.shopcars.application.services.ICustomerService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest

@Service
class CustomerServiceImpl(private val customerDao: ICustomerDao) : ICustomerService {
    override fun getCustomers(): List<Customer> = customerDao.findAll()

    override fun findCustomerByID(id: Long): Customer? = customerDao.findByIdOrNull(id)

    override fun saveCustomer(customer: Customer): Customer {
        customer.password = BCryptPasswordEncoder().encode(customer.password)
        return customerDao.save(customer)
    }

    override fun findCustomerByEmail(email: String): Customer? = customerDao.findOneByEmail(email)

    override fun deleteCustomerById(id: Long) = customerDao.deleteById(id)

    override fun getJWT(customer: Customer, request: HttpServletRequest): String {
        return JWTAuthorizationFilter().createJWT(customer.email,customer.id,customer.role,request)
    }

    override fun login(email: String, password: String): Customer {
        return this.findCustomerByEmail(email)?.apply {
            if (BCryptPasswordEncoder().matches(password, this.password)) {
                return this
            } else {
                throw EntityNotFoundException("Your username or password is incorrect")
            }
        } ?: throw EntityNotFoundException("Your username or password is incorrect")
    }
    override fun updateCustomer(customer: Customer): Customer {
        return if(customerDao.existsById(customer.id)) customerDao.save(customer)
        else throw  EntityNotFoundException("Customer id:${customer.id} does not exists")
    }
}