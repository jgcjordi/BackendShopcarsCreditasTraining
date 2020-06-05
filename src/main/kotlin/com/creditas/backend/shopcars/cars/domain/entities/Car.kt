package com.creditas.backend.shopcars.cars.domain.entities

import com.creditas.backend.shopcars.application.domain.entities.Customer
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
        var price: Float = 0f,
        var number_plate: String = "0000 ABC",
        var fuel_type: Int = 0,
        var km:Int = 0,
        var color: Int = 0,
        var year: Int =0,
        var url_image: String = "https://www.pinclipart.com/picdir/big/174-1749268_cartoon-sport-car-sports-car-cartoon-png-clipart.png",
        @JsonManagedReference(value="model")
        @NotNull
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="model_id")
        var model: Model = Model(),
        @ManyToMany(mappedBy = "purchaser_car")
        @JsonBackReference(value="customer_purchaser_car")
        var purchaser: MutableList<Customer> = mutableListOf<Customer>(),
        @ManyToMany(mappedBy = "seller_car")
        @JsonBackReference(value="customer_seller_car")
        var seller: MutableList<Customer> = mutableListOf<Customer>(),
        @NotNull
        var create_at: LocalDateTime = LocalDateTime.now(),
        var update_at: LocalDateTime = LocalDateTime.now()
)
