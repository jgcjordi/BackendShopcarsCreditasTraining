package com.creditas.backend.shopcars.application.services

import com.creditas.backend.shopcars.application.domain.entities.UserShop
import javax.servlet.http.HttpServletRequest

interface IUserService {
    fun getUsers(): List<UserShop>
    fun findUserByID(idUser:Long): UserShop?
    fun saveUser(userShop:UserShop): UserShop
    fun updateUser(userShop: UserShop): UserShop
    fun findUserByEmail(email:String): UserShop?
    fun deleteUserById(idUser:Long)
    fun getJWT(userShop: UserShop, request: HttpServletRequest):String
    fun login(email:String,password:String):UserShop
}