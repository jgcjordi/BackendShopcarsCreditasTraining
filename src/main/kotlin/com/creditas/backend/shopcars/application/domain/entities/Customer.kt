package com.creditas.backend.shopcars.application.domain.entities

import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.sun.istack.NotNull
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "customer")
data class Customer(
                @Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
                var id : Long = 0,
                var role: String = "USER",
                var name: String?,
                var surname: String?,
                var identification: String?,
                var birthday: LocalDate?,
                @NotNull
                @Column(unique = true)
                var email: String,
                var password: String,
                @ManyToMany(cascade = [CascadeType.ALL],fetch = FetchType.LAZY)
                @JoinTable(name = "customer_purchaser_car",
                        joinColumns = [JoinColumn(name = "customer_id", referencedColumnName = "id")],
                        inverseJoinColumns = [JoinColumn(name = "car_id", referencedColumnName = "id")])
                @JsonManagedReference(value="customer_purchaser_car")
                var purchaser_car: MutableList<Car> = mutableListOf<Car>(),
                @ManyToMany(cascade = [CascadeType.ALL],fetch = FetchType.LAZY)
                @JoinTable(name = "customer_seller_car",
                        joinColumns = [JoinColumn(name = "customer_id", referencedColumnName = "id")],
                        inverseJoinColumns = [JoinColumn(name = "car_id", referencedColumnName = "id")])
                @JsonManagedReference(value="customer_seller_car")
                var seller_car: MutableList<Car> = mutableListOf<Car>(),
                @NotNull
                var create_at: LocalDateTime = LocalDateTime.now(),
                var update_at: LocalDateTime = LocalDateTime.now())