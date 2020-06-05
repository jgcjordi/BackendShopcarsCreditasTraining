package com.creditas.backend.shopcars

import com.creditas.backend.shopcars.application.domain.entities.Customer
import com.creditas.backend.shopcars.application.infraestructure.security.JWTAuthorizationFilter
import com.creditas.backend.shopcars.application.services.implementation.CustomerServiceImpl
import com.creditas.backend.shopcars.cars.domain.entities.Brand
import com.creditas.backend.shopcars.cars.domain.entities.Car
import com.creditas.backend.shopcars.cars.domain.entities.Model
import com.creditas.backend.shopcars.cars.services.implementation.BrandServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.CarServiceImpl
import com.creditas.backend.shopcars.cars.services.implementation.ModelServiceImpl
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import java.time.LocalDate

@SpringBootApplication
class ShopCarsApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ShopCarsApplication>(*args)
        }
    }

    @EnableWebSecurity
    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    class WebSecurityConfig : WebSecurityConfigurerAdapter() {
        override fun configure(httpSecurity: HttpSecurity) {
            httpSecurity
                    .cors()
                    .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers(
                            "/api/v1/customers/login",
                            "/api/v1/customers/save").permitAll()
                    .antMatchers(
                            "/api/v1/cars/open/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilterBefore(JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<ShopCarsApplication>(*args)
}

@Component
class OnBoot(
        private val carService: CarServiceImpl,
        private val modelService: ModelServiceImpl,
        private val brandService: BrandServiceImpl,
        private val customerService: CustomerServiceImpl) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val ford = brandService.saveBrand(Brand(name = "Ford"))
        val mustang = modelService.saveModel(Model(name = "Mustang", brand = ford))
        val fiesta = modelService.saveModel(Model(name = "Fiesta", brand = ford))
        val focus = modelService.saveModel(Model(name = "Focus", brand = ford))
        val carMustang = carService.saveCar(Car(number_plate = "2385 AYR", model = mustang, km = 50000, price = 300000F, color = "Rojo", url_image = "https://cdn.topgear.es/sites/navi.axelspringer.es/public/styles/1200/public/media/image/2017/05/prueba-ford-mustang-gt_19.jpg?itok=037w0axH"))
        val carMustang2 = carService.saveCar(Car(number_plate = "4362 LYR", model = mustang, km = 12500, price = 325000F, color = "Rojo", url_image = "https://cdn.topgear.es/sites/navi.axelspringer.es/public/styles/1200/public/media/image/2017/05/prueba-ford-mustang-gt_19.jpg?itok=037w0axH"))
        val carFiesta = carService.saveCar(Car(number_plate = "4577 JGC", model = fiesta, km = 45030, price = 20000F, color = "Azul", url_image = "https://static.motor.es/fotos-noticias/2018/05/ford-fiesta-rs-descartado-201846985_1.jpg"))
        val carFiesta2 = carService.saveCar(Car(number_plate = "4732 GHJ", model = fiesta, km = 200000, price = 4000F, color = "Azul", url_image = "https://static.motor.es/fotos-noticias/2018/05/ford-fiesta-rs-descartado-201846985_1.jpg"))
        val carFocus = carService.saveCar(Car(number_plate = "5689 ZJH", model = focus, km = 5050, price = 15000F, color = "Azul", url_image = "https://www.coches.com/fotos_historicas/ford/Focus-ST-Line-2018/high_ford_focus-st-line-2018_r34.jpg"))

        val bmw = brandService.saveBrand(Brand(name = "BMW"))
        val x1 = modelService.saveModel(Model(name = "X1", brand = bmw))
        val s1 = modelService.saveModel(Model(name = "S1", brand = bmw))
        val m3 = modelService.saveModel(Model(name = "M3", brand = bmw))
        val carX1 = carService.saveCar(Car(number_plate = "5378 DTG", model = x1, km = 1200, price = 24000F, color = "Blanco", url_image ="https://www.topgear.es/sites/topgear.es/public/styles/855/public/dc/fotos/BMW-X1-2016-D01.jpg?itok=xea3DBnS"))
        val carS1 = carService.saveCar(Car(number_plate = "4528 BWG", model = s1, km = 24600, price = 14000F, color = "Blanco", url_image ="https://img.motor16.com/modelos/bmw-serie-1.jpg"))
        val carM3 = carService.saveCar(Car(number_plate = "7524 ABS", model = m3, km = 15000, price = 44000F, color = "Azul", url_image ="https://cdn.topgear.es/sites/navi.axelspringer.es/public/styles/1200/public/media/image/2019/09/bmw-m3-2020.jpg?itok=AEZtVXp5"))

        val nissan = brandService.saveBrand(Brand(name = "Nissan"))
        val qashqai = modelService.saveModel(Model(name = "QASHQAI", brand = nissan))
        val juke = modelService.saveModel(Model(name = "Juke", brand = nissan))
        val carQashqai = carService.saveCar(Car(number_plate = "2475 EJR", model = qashqai, km = 3500, price = 21000F, color = "Blanco", url_image ="https://a.ccdn.es/cnet/contents/media/nissan/qashqai/1234353.jpg/0_122_1280_842//937x624cut/"))
        val carJuke = carService.saveCar(Car(number_plate = "2565 LKR", model = juke, km = 0, price = 25000F, color = "Rojo", url_image ="https://imagenes-cdn.autofacil.es/multimedia/fotos/2019/10/09/170354/preview2col_mg.jpg?t=1570621209000"))

        val user1 = customerService.saveCustomer(Customer(purchaser_car = mutableListOf(carMustang), name = "Admin", surname = "Admin", identification = "1234A", birthday = LocalDate.of(2017, 1, 13), email = "admin@admin.com", password = "admin"))



        println(carService.findAllCars()[0].number_plate)
    }
}

