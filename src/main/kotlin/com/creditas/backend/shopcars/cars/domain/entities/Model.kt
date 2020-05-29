package com.creditas.backend.shopcars.cars.domain.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="model")
data class Model (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long = 0,
    var name: String = "name",
    @JsonManagedReference(value="brand")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brand_id")
    var brand: Brand,
    @JsonBackReference(value="model")
    @OneToMany(mappedBy= "model", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var car: List<Car> = listOf(),
    var create_at: LocalDateTime = LocalDateTime.now(),
    var update_at: LocalDateTime = LocalDateTime.now()
)