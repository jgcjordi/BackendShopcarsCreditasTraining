package com.creditas.backend.shopcars.application.infraestructure.controller

import com.creditas.backend.shopcars.application.domain.entities.User
import com.creditas.backend.shopcars.application.services.implementation.UserServiceImpl
import com.creditas.backend.shopcars.cars.domain.entities.Car
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
    fun findById(@PathVariable id: Long): ResponseEntity<User> {
        val entity = userService.findUserByID(id)
        return ResponseEntity.status(
                if (entity != null) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }

    @GetMapping("/save")
    fun save(@RequestBody user: User) = ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.saveUser(user))

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) = userService.deleteUserById(id)

    @PutMapping
    fun update(@RequestBody user: User) = userService.updateUser(user)

    @PostMapping("/login")
    fun login(@RequestBody user: User, request: HttpServletRequest): ResponseEntity<String> {
            var logedUser = userService.login(user.email, user.password)
            var token: String = userService.getJWT(logedUser, request)
            return ResponseEntity(token, HttpStatus.OK)

    }
}