package com.creditas.backend.shopcars.cars.domain.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.sun.istack.NotNull
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="brand")
data class Brand (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id : Long?,
        var name:String,
        @JsonBackReference(value="brand")
        @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)], orphanRemoval = true)
        var model: List<Model>? = null,
        @NotNull
        var create_at: LocalDateTime? = LocalDateTime.now(),
        var update_at: LocalDateTime?= null
){

}
