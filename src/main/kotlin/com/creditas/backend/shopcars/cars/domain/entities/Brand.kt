package com.creditas.backend.shopcars.cars.domain.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="brand")
data class Brand (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id : Long = 0,
        var name:String = "name",
        @JsonBackReference(value="brand")
        @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)], orphanRemoval = true)
        var model: List<Model> = listOf(),
        var create_at: LocalDateTime = LocalDateTime.now(),
        var update_at: LocalDateTime = LocalDateTime.now()
){

}
