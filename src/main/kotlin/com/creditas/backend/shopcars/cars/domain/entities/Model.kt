package com.creditas.backend.shopcars.cars.domain.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="model")
data class Model (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long?,
    var name: String,
    @JsonManagedReference(value="brand")
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brand_id")
    var brand: Brand? = null,
    @JsonBackReference(value="model")
    @OneToMany(mappedBy= "model", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var car: List<Car>? = null,
    @NotNull
    var create_at: LocalDateTime? = LocalDateTime.now(),
    var update_at: LocalDateTime? = null
)