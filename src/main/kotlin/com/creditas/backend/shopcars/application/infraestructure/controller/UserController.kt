package com.creditas.backend.shopcars.application.infraestructure.controller

import com.creditas.backend.shopcars.application.domain.entities.UserShop
import com.creditas.backend.shopcars.application.services.implementation.UserServiceImpl
import org.apache.juli.logging.LogFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@CrossOrigin
@RequestMapping("api/v1/users")
class UserController (private val userService: UserServiceImpl){
    private val LOGGER = LogFactory.getLog("UserController.class")

    @GetMapping
    fun findAllUsers() = userService.getUsers()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<UserShop> {
        val entity = userService.findUserByID(id)
        return ResponseEntity.status(
                if (entity != null) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }

    @GetMapping("/save")
    fun save(@RequestBody userShop: UserShop) = ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.saveUser(userShop))

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) = userService.deleteUserById(id)

    @PutMapping
    fun update(@RequestBody userShop: UserShop) = userService.updateUser(userShop)

    @PostMapping("/login")
    fun login(@RequestBody userShop: UserShop, request: HttpServletRequest): ResponseEntity<String> {
            var logedUser = userService.login(userShop.email, userShop.password)
            var token: String = userService.getJWT(logedUser, request)
            return ResponseEntity(token, HttpStatus.OK)

    }
}