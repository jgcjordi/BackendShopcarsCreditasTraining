package com.creditas.backend.shopcars.cars.domain.dao

import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.domain.entities.Model
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository


@Repository
interface ICarDao: PagingAndSortingRepository<Car, Long>{
    fun findByPurchaserIsNull(pageable: Pageable): Page<Car>
    fun findByModelAndPurchaserIsNull(model:Model, pageable: Pageable): Page<Car>

    @Query("select * from car  " +
            "inner join model on car.model_id = model.id " +
            "left join customer_purchaser_car on car.id = customer_purchaser_car.car_id where model.brand_id = ?1 and customer_purchaser_car.car_id isnull",
            nativeQuery = true)
    fun findAllCarsNoPurchasedOfBrand(id: Long, pageable: Pageable):Page<Car>

    @Query("select * from car " +
            "inner join customer_purchaser_car on car.id = customer_purchaser_car.car_id " +
            " where customer_purchaser_car.customer_id = ?1",
            nativeQuery = true)
    fun findPurcharseCars(customerId: Long):List<Car>


    @Query("select * from car " +
            "inner join customer_seller_car on car.id = customer_seller_car.car_id " +
            " where customer_seller_car.customer_id = ?1",
            nativeQuery = true)
    fun findSellerCars(customerId: Long):List<Car>
}