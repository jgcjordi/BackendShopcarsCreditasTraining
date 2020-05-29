package com.creditas.backend.shopcars.cars.domain.entities

import com.creditas.backend.shopcars.application.domain.entities.User
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="car")
data class Car (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id : Long = 0,
        var name:String,
        var price: Float,
        var number_plate: String,
        var fuel_type: Int,
        var km:Int,
        var color: String,
        @JsonManagedReference(value="model")
        @NotNull
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="model_id")
        var model: Model,
        @ManyToMany(mappedBy = "purchaser_car")
        @JsonBackReference(value="user_purchaser_car")
        var purchaser: MutableList<User> = mutableListOf<User>(),
        @ManyToMany(mappedBy = "seller_car")
        @JsonBackReference(value="user_seller_car")
        var seller: MutableList<User> = mutableListOf<User>(),
        @NotNull
        var create_at: LocalDateTime = LocalDateTime.now(),
        var update_at: LocalDateTime = LocalDateTime.now()
)
