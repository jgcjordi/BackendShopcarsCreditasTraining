package com.creditas.backend.shopcars.application.services.implementation

import com.creditas.backend.shopcars.application.domain.dao.IUserDao
import com.creditas.backend.shopcars.application.domain.entities.UserShop
import com.creditas.backend.shopcars.application.infraestructure.security.JWTAuthorizationFilter
import com.creditas.backend.shopcars.application.services.IUserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest

@Service
class UserServiceImpl(private val UserDao: IUserDao) : IUserService {
    override fun getUsers(): List<UserShop> = UserDao.findAll()

    override fun findUserByID(id: Long): UserShop? = UserDao.findByIdOrNull(id)

    override fun saveUser(userShop: UserShop): UserShop {
        userShop.password = BCryptPasswordEncoder().encode(userShop.password)
        return UserDao.save(userShop)
    }

    override fun findUserByEmail(email: String): UserShop? = UserDao.findOneByEmail(email)

    override fun deleteUserById(id: Long) = UserDao.deleteById(id)

    override fun getJWT(userShop: UserShop, request: HttpServletRequest): String {
        return JWTAuthorizationFilter().createJWT(userShop.email,userShop.id,userShop.role,request)
    }

    override fun login(email: String, password: String): UserShop {
        return this.findUserByEmail(email)?.apply {
            if (BCryptPasswordEncoder().matches(password, this.password)) {
                return this
            } else {
                throw EntityNotFoundException("Your username or password is incorrect")
            }
        } ?: throw EntityNotFoundException("Your username or password is incorrect")
    }
    override fun updateUser(userShop: UserShop): UserShop {
        return if(UserDao.existsById(userShop.id)) UserDao.save(userShop)
        else throw  EntityNotFoundException("User id:${userShop.id} does not exists")
    }
}