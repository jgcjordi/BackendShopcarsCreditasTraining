package com.creditas.backend.shopcars.application.domain.dao

import com.creditas.backend.shopcars.application.domain.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface IUserDao: JpaRepository<User, Long>{
    fun findOneByEmail(email: String): User
}