package com.creditas.backend.shopcars.cars.domain.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
data class Brand(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        var name:String = "name",
        var createAt: LocalDateTime = LocalDateTime.now(),
        var updateAt: LocalDateTime = LocalDateTime.now(),

        @JsonBackReference
        @OneToMany(mappedBy = "brand", cascade = [(CascadeType.ALL)], orphanRemoval = true)
        var models: List<Model> = listOf()
)
