package com.creditas.backend.shopcars.cars.domain.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
data class Car(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        var name: String = "name",
        var price: Float = 0f,
        var numberPlate: String = "0000 ABC",
        var fuelType: Int = 0,
        var km: Int = 0,
        var color: String = "color",
        var createAt: LocalDateTime = LocalDateTime.now(),
        var updateAt: LocalDateTime = LocalDateTime.now(),

        @JsonManagedReference
        @ManyToOne
        @JoinColumn
        var model: Model
)
