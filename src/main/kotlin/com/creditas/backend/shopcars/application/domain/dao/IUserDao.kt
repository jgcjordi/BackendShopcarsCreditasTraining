package com.creditas.backend.shopcars.application.domain.dao

import com.creditas.backend.shopcars.application.domain.entities.UserShop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface IUserDao: JpaRepository<UserShop, Long>{
    fun findOneByEmail(email: String): UserShop
}