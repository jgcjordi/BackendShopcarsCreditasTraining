package com.creditas.backend.shopcars.application.services

import com.creditas.backend.shopcars.application.domain.entities.User
import javax.servlet.http.HttpServletRequest

interface IUserService {
    fun getUsers(): List<User>
    fun findUserByID(idUser:Long): User?
    fun saveUser(user:User): User
    fun updateUser(user: User): User
    fun findUserByEmail(email:String): User?
    fun deleteUserById(idUser:Long)
    fun getJWT(user: User, request: HttpServletRequest):String
    fun login(email:String,password:String):User
}