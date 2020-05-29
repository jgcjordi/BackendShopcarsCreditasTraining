package com.creditas.backend.shopcars.application.domain.entities

import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.sun.istack.NotNull
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "user")
data class User(
                @Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
                var id : Long = 0,
                var role: String = "USER",
                var name: String?,
                var surname: String?,
                var birthday: LocalDate?,
                @NotNull
                @Column(unique = true)
                var email: String,
                var password: String,
                @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
                @JoinTable(name = "user_purchaser_car",
                        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                        inverseJoinColumns = [JoinColumn(name = "car_id", referencedColumnName = "id")])
                @JsonManagedReference(value="user_purchaser_car")
                var purchaser_car: MutableList<Car> = mutableListOf<Car>(),
                @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
                @JoinTable(name = "user_seller_car",
                        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                        inverseJoinColumns = [JoinColumn(name = "car_id", referencedColumnName = "id")])
                @JsonManagedReference(value="user_seller_car")
                var seller_car: MutableList<Car> = mutableListOf<Car>())