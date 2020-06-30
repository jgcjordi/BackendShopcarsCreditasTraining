package com.creditas.backend.shopcars.application.infraestructure.controller

import com.creditas.backend.shopcars.application.domain.entities.Customer
import com.creditas.backend.shopcars.application.infraestructure.security.JWTAuthorizationFilter
import com.creditas.backend.shopcars.application.services.implementation.CustomerServiceImpl
import io.jsonwebtoken.Claims
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@CrossOrigin
@RequestMapping("api/v1/customers")
class CustomerController(private val customerService: CustomerServiceImpl) {

    fun getCustomerByToken(request: HttpServletRequest): Customer? {

        val claims: Claims = JWTAuthorizationFilter().validateJWT(request)

        return customerService.findCustomerByID((claims["id"] as Int).toLong())
    }

    @GetMapping
    fun findAllUsers() = customerService.getCustomers()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Customer> {
        val entity = customerService.findCustomerByID(id)
        return ResponseEntity.status(
                if (entity != null) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }

    @PostMapping("/save")
    fun save(@RequestBody customer: Customer) = ResponseEntity.status(HttpStatus.CREATED)
            .body(customerService.saveCustomer(customer))

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) = customerService.deleteCustomerById(id)

    @PutMapping
    fun update(@RequestBody customer: Customer) = customerService.updateCustomer(customer)

    @PostMapping("/login")
    fun login(@RequestBody customer: Customer, request: HttpServletRequest): ResponseEntity<String> {
        return try{
            val loggedUser = customerService.login(customer.email, customer.password)
            val token = customerService.getJWT(loggedUser, request)
            ResponseEntity(token, HttpStatus.OK)
        }
        catch (e:Exception){

            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }
}