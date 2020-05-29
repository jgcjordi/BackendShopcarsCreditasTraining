package com.creditas.backend.shopcars.application.services

import com.creditas.backend.shopcars.application.domain.entities.User
import javax.servlet.http.HttpServletRequest

interface IUserService {
    fun getUsers(): List<User>
    fun findUserByID(idUser:Long): User?
    fun addUser(user:User): User
    fun findUserByEmail(email:String): User?
    fun deleteUser(idUser:Long)
    fun getJWT(email:String,id:Long,role: String, request: HttpServletRequest):String
    fun login(email:String,password:String):User
}