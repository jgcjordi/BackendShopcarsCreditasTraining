package com.creditas.backend.shopcars.application.services.implementation

import com.creditas.backend.shopcars.application.domain.dao.IUserDao
import com.creditas.backend.shopcars.application.domain.entities.User
import com.creditas.backend.shopcars.application.infraestructure.security.JWTAuthorizationFilter
import com.creditas.backend.shopcars.application.services.IUserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest

@Service
class UserServiceImpl(private val UserDao: IUserDao) : IUserService {
    override fun getUsers(): List<User> = UserDao.findAll()

    override fun findUserByID(id: Long): User? = UserDao.findByIdOrNull(id)

    override fun saveUser(user: User): User {
        user.password = BCryptPasswordEncoder().encode(user.password)
        return UserDao.save(user)
    }

    override fun findUserByEmail(email: String): User? = UserDao.findOneByEmail(email)

    override fun deleteUserById(id: Long) = UserDao.deleteById(id)

    override fun getJWT(user: User, request: HttpServletRequest): String {
        return JWTAuthorizationFilter().createJWT(user.email,user.id,user.role,request)
    }

    override fun login(email: String, password: String): User {
        return this.findUserByEmail(email)?.apply {
            if (BCryptPasswordEncoder().matches(password, this.password)) {
                return this
            } else {
                throw EntityNotFoundException("Your username or password is incorrect")
            }
        } ?: throw EntityNotFoundException("Your username or password is incorrect")
    }
    override fun updateUser(user: User): User {
        return if(UserDao.existsById(user.id)) UserDao.save(user)
        else throw  EntityNotFoundException("User id:${user.id} does not exists")
    }
}