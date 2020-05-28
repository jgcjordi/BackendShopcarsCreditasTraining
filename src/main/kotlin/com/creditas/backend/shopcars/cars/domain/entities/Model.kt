package com.creditas.backend.shopcars.cars.domain.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
data class Model (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long = 0,
    var name: String = "name",
    var createAt: LocalDateTime = LocalDateTime.now(),
    var updateAt: LocalDateTime = LocalDateTime.now(),

    @JsonManagedReference
    @ManyToOne
    @JoinColumn
    var brand: Brand,

    @JsonBackReference
    @OneToMany(mappedBy= "model", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var cars: List<Car> = listOf()
)