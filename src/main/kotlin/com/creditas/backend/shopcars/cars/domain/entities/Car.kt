package com.creditas.backend.shopcars.cars.domain.entities

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
        @NotNull
        var create_at: LocalDateTime = LocalDateTime.now(),
        var update_at: LocalDateTime = LocalDateTime.now()
)
