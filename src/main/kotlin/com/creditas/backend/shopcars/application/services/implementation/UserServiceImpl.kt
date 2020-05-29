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
class UserServiceImpl(private val IUserDao: IUserDao) : IUserService {
    override fun getUsers(): List<User> = IUserDao.findAll()

    override fun findUserByID(id: Long): User? = IUserDao.findByIdOrNull(id)

    override fun addUser(user: User): User {
        user.password = BCryptPasswordEncoder().encode(user.password)
        return IUserDao.save(user)
    }

    override fun findUserByEmail(email: String): User? = IUserDao.findOneByEmail(email)

    override fun deleteUser(id: Long) = IUserDao.deleteById(id)

    override fun getJWT(email: String, id: Long, role: String, request: HttpServletRequest): String {
        return JWTAuthorizationFilter().createJWT(email,id,role,request)
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
}