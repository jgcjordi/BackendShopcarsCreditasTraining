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
import org.apache.juli.logging.LogFactory
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
import java.io.File
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
                    .antMatchers(
                            "/api/v1/images/get/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilterBefore(JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        }
    }
}

//fun main(args: Array<String>) {
//    runApplication<ShopCarsApplication>(*args)
//}

@Component
class OnBoot(
        private val carService: CarServiceImpl,
        private val modelService: ModelServiceImpl,
        private val brandService: BrandServiceImpl,
        private val customerService: CustomerServiceImpl) : ApplicationRunner {

    private val LOGGER = LogFactory.getLog("OnBoot.class")
    override fun run(args: ApplicationArguments?) {

        //Creación de la carpeta de imagenes si no existe
        val fileBasePath = "src/main/resources/images"
        val directory = File(fileBasePath)
        if (! directory.exists()){
            directory.mkdir()
            LOGGER.warn("no existe")
        }else{
            LOGGER.warn("existe")
        }

        val ford = brandService.saveBrand(Brand(name = "Ford"))
        val mustang = modelService.saveModel(Model(name = "Mustang", brand = ford))
        val fiesta = modelService.saveModel(Model(name = "Fiesta", brand = ford))
        val focus = modelService.saveModel(Model(name = "Focus", brand = ford))
        val sportbreak = modelService.saveModel(Model(name = "Sportbreak", brand = ford))
        val carMustang = carService.saveCar(Car(number_plate = "2385 AYR", model = mustang, km = 50000,fuel_type = 2, price = 300000F, color = 9, year = 2010,  url_image = "https://cdn.topgear.es/sites/navi.axelspringer.es/public/styles/1200/public/media/image/2017/05/prueba-ford-mustang-gt_19.jpg?itok=037w0axH"))
        val carMustang2 = carService.saveCar(Car(number_plate = "4362 LYR", model = mustang, km = 42500,fuel_type = 5, price = 325000F, color = 9, year = 2015, url_image = "https://cdn.topgear.es/sites/navi.axelspringer.es/public/styles/1200/public/media/image/2017/05/prueba-ford-mustang-gt_19.jpg?itok=037w0axH"))
        val carFiesta = carService.saveCar(Car(number_plate = "4577 JGC", model = fiesta, km = 25030,fuel_type = 1, price = 20000F, color = 1, year = 2014, url_image = "https://static.motor.es/fotos-noticias/2018/05/ford-fiesta-rs-descartado-201846985_1.jpg"))
        val carFiesta2 = carService.saveCar(Car(number_plate = "4732 GHJ", model = fiesta, km = 20000,fuel_type = 4, price = 4000F, color = 3, year = 2013, url_image = "https://www.coches.com/fotos_historicas/ford/Fiesta/6457c76c211873fb8d44b2345dae758f.jpg"))
        val carFocus = carService.saveCar(Car(number_plate = "5689 ZJH", model = focus, km = 5050, price = 15000F,fuel_type = 1, color = 7, year = 2012, url_image = "https://www.coches.com/fotos_historicas/ford/Focus-ST-CJ-Pony-Parts-2015/ford_focus-st-cj-pony-parts-2015_r1.jpg"))
        val carFocus2 = carService.saveCar(Car(number_plate = "5259 FWZ", model = focus, km = 14525, price = 4000F,fuel_type = 2, color = 1, year = 2014,url_image = "https://www.coches.com/fotos_historicas/ford/Focus-ST-Line-2018/high_ford_focus-st-line-2018_r34.jpg"))
        val carFocus3 = carService.saveCar(Car(number_plate = "5665 FSW", model = focus, km = 32258, price = 21000F,fuel_type = 3, color = 3, year = 2016, url_image = "https://imagenes-cdn.autofacil.es/multimedia/fotos/2018/04/12/122594/widget_g.jpg    "))
        val carFocus4 = carService.saveCar(Car(number_plate = "9652 FSS", model = sportbreak, km = 47925, price = 29000F,fuel_type = 4, color = 4, year = 2017,  url_image = "https://www.coches.com/fotos_historicas/ford/Focus-Vignale-Turnier-2018/ford_focus-vignale-turnier-2018_r40.jpg"))


        val bmw = brandService.saveBrand(Brand(name = "BMW"))
        val x1 = modelService.saveModel(Model(name = "X1", brand = bmw))
        val s1 = modelService.saveModel(Model(name = "S1", brand = bmw))
        val m3 = modelService.saveModel(Model(name = "M3", brand = bmw))
        val carX1 = carService.saveCar(Car(number_plate = "5378 DTG", model = x1, km = 14000, price = 24000F,fuel_type = 1, color = 5, year = 2018, url_image ="https://www.coches.com/fotos_historicas/bmw/X1/8df0e4c7f73e6662d4a0e7a9b99dc434.jpg"))
        val carX11 = carService.saveCar(Car(number_plate = "7966 TJG", model = x1, km = 12000, price = 28000F,fuel_type = 5, color = 3, year = 2019,  url_image ="https://www.topgear.es/sites/topgear.es/public/styles/855/public/dc/fotos/BMW-X1-2016-D01.jpg?itok=xea3DBnS"))

        val carS1 = carService.saveCar(Car(number_plate = "4528 BWG", model = s1, km = 24600, price = 14000F,fuel_type = 3, color = 3, year = 2017, url_image ="https://img.motor16.com/modelos/bmw-serie-1.jpg"))
        val carS11 = carService.saveCar(Car(number_plate = "6555 WER", model = s1, km = 25700, price = 15000F,fuel_type = 5, color = 5, year = 2019, url_image ="https://www.coches.com/fotos_historicas/bmw/1-Series-120d-Sport-Line-3-door-F21-2017/bmw_120d-sport-line-3-door-f21-2017_r33.jpg"))


        val carM3 = carService.saveCar(Car(number_plate = "7524 ABS", model = m3, km = 15000, price = 44000F,fuel_type = 1, color = 1, year = 2014, url_image ="https://hackercar.com/wp-content/uploads/2020/02/bmw-M340d-1.jpg"))
        val carM33 = carService.saveCar(Car(number_plate = "8554 WHF", model = m3, km = 45000, price = 54000F,fuel_type = 5, color = 1, year = 2013, url_image ="https://cdn.topgear.es/sites/navi.axelspringer.es/public/styles/1200/public/media/image/2019/09/bmw-m3-2020.jpg?itok=AEZtVXp5"))

        val nissan = brandService.saveBrand(Brand(name = "Nissan"))
        val qashqai = modelService.saveModel(Model(name = "QASHQAI", brand = nissan))
        val juke = modelService.saveModel(Model(name = "Juke", brand = nissan))
        val carQashqai = carService.saveCar(Car(number_plate = "2475 EJR", model = qashqai, km = 3500,fuel_type = 1, price = 21000F, color = 3, year = 2012, url_image ="https://a.ccdn.es/cnet/contents/media/nissan/qashqai/1234353.jpg/0_122_1280_842//937x624cut/"))
        val carJuke = carService.saveCar(Car(number_plate = "7521 LKR", model = juke, km = 7000, price = 25000F,fuel_type = 1, color = 4, year = 2014, url_image ="https://imagenes-cdn.autofacil.es/multimedia/fotos/2019/10/09/170354/preview2col_mg.jpg?t=1570621209000"))
        val carJuke1 = carService.saveCar(Car(number_plate = "2855 LKR", model = juke, km = 6000, price = 38500F,fuel_type = 2, color = 0, year = 2015, url_image ="https://www.coches.com/fotos_historicas/nissan/Juke/nissan_juke-2014_r4.jpg"))

        val seat = brandService.saveBrand(Brand(name = "Seat"))
        val leon = modelService.saveModel(Model(name = "Leon", brand = seat))
        val ibiza = modelService.saveModel(Model(name = "Ibiza", brand = seat))
        val ateca = modelService.saveModel(Model(name = "Ateca", brand = seat))
        val carLeon = carService.saveCar(Car(number_plate = "4458 ADS", model = leon, km = 54000,fuel_type = 2, price = 30000F, color = 8, year = 2010,  url_image = "https://www.coches.com/fotos_historicas/seat/Leon-Cupra-2006/seat_leon-cupra-2006_r15.jpg"))
        val carLeon2 = carService.saveCar(Car(number_plate = "6698 EHT", model = leon, km = 49800,fuel_type = 5, price = 32500F, color = 5, year = 2015, url_image = "https://www.coches.com/fotos_historicas/seat/Leon-Cupra-R-2017/seat_leon-cupra-r-2017_r2.jpg"))
        val carLeon3 = carService.saveCar(Car(number_plate = "6674 JHF", model = leon, km = 66528, price = 29000F,fuel_type = 4, color = 3, year = 2017,  url_image = "https://www.coches.com/fotos_historicas/seat/Leon-Cupra/seat_leon-cupra-2014_r1.jpg"))
        val carIbiza = carService.saveCar(Car(number_plate = "8895 KLP", model = ibiza, km = 18962,fuel_type = 1, price = 11000F, color = 9, year = 2014, url_image = "https://www.coches.com/fotos_historicas/seat/Ibiza/79553a7c21dd0eae46c9d916f00e8b59.jpg"))
        val carIbiza2 = carService.saveCar(Car(number_plate = "5577 BCD", model = ibiza, km = 34500,fuel_type = 4, price = 5000F, color = 2, year = 2013, url_image = "https://www.coches.com/fotos_historicas/seat/Ibiza/a7ae03aa22e99886f038c14d06fc5128.jpg"))
        val carIbiza3 = carService.saveCar(Car(number_plate = "4598 HTC", model = ibiza, km = 7952, price = 15000F,fuel_type = 1, color = 5, year = 2012, url_image = "https://www.coches.com/fotos_historicas/seat/Ibiza/09a52188dda98f646f5d425530a82791.jpg"))
        val carAteca = carService.saveCar(Car(number_plate = "6752 ZOP", model = ateca, km = 35689, price = 4000F,fuel_type = 2, color = 5, year = 2014,url_image = "https://www.coches.com/fotos_historicas/seat/Ateca-2016/seat_ateca-2016_r10.jpg"))
        val carAteca1 = carService.saveCar(Car(number_plate = "3475 NIR", model = ateca, km = 28600, price = 22000F,fuel_type = 3, color = 3, year = 2016, url_image = "https://www.coches.com/fotos_historicas/seat/Ateca-2016/seat_ateca-2016_r17.jpg"))

        val mercedes = brandService.saveBrand(Brand(name = "Mercedes"))
        val claseA = modelService.saveModel(Model(name = "Clase A", brand = mercedes))
        val claseC = modelService.saveModel(Model(name = "Clase C", brand = mercedes))
        val claseGLA = modelService.saveModel(Model(name = "GLA", brand = mercedes))
        val carClaseA = carService.saveCar(Car(number_plate = "452F SDE", model = claseA, km = 65000,fuel_type = 2, price = 25000F, color = 9, year = 2010,  url_image = "https://www.coches.com/fotos_historicas/amg/Mercedes-A45-4MATIC-W176-2015/amg_mercedes-a45-4matic-w176-2015_r8.jpg"))
        val carClaseA1 = carService.saveCar(Car(number_plate = "6698 EHT", model = claseA, km = 45780,fuel_type = 5, price = 37500F, color = 5, year = 2015, url_image = "https://www.coches.com/fotos_historicas/mercedes/A-Klasse-AMG-Line-W177-2018/mercedes_a-klasse-amg-line-w177-2018_r17.jpg"))
        val carClaseA2 = carService.saveCar(Car(number_plate = "6674 JHF", model = claseA, km = 61400, price = 34000F,fuel_type = 4, color = 9, year = 2017,  url_image = "https://www.coches.com/fotos_historicas/amg/Mercedes-A-Klasse-A45-W176-Australia/amg_mercedes-a-klasse-a45-w176-australia-2013_r30.jpg"))
        val carClaseC = carService.saveCar(Car(number_plate = "8895 KLP", model = claseC, km = 19000,fuel_type = 1, price = 17000F, color = 1, year = 2014, url_image = "https://www.coches.com/fotos_historicas/mercedes/C-Klasse-C200-Coupe-AMG-Line-C205-Australia-2016/mercedes_c-klasse-c200-coupe-amg-line-c205-australia-2016_r17.jpg"))
        val carClaseC1 = carService.saveCar(Car(number_plate = "5577 BCD", model = claseC, km = 37000,fuel_type = 4, price = 6000F, color = 3, year = 2013, url_image = "https://www.coches.com/fotos_historicas/amg/Mercedes-C63-S-Coupe-C205-2018/amg_mercedes-c63-s-coupe-c205-2018_r18.jpg"))
        val carClaseC2 = carService.saveCar(Car(number_plate = "4598 HTC", model = claseC, km = 8000, price = 27000F,fuel_type = 1, color = 8, year = 2012, url_image = "https://www.coches.com/fotos_historicas/amg/Mercedes-C43-4MATIC-Coupe-C205-2018/amg_mercedes-c43-4matic-coupe-c205-2018_r32.jpg"))
        val carGLA = carService.saveCar(Car(number_plate = "6752 ZOP", model = claseGLA, km = 34000, price = 40000F,fuel_type = 2, color = 3, year = 2014,url_image = "https://www.coches.com/fotos_historicas/mercedes/Clase-GLA/34511b0800378c3881e7c0850a51ebc6.jpg"))
        val carGLA1 = carService.saveCar(Car(number_plate = "3475 NIR", model = claseGLA, km = 26000, price = 28000F,fuel_type = 3, color = 6, year = 2016, url_image = "https://www.coches.com/fotos_historicas/mercedes/Clase-GLA/4a615f629fcafe94ddb1ba0c5d899983.jpg"))



        val user1 = customerService.saveCustomer(Customer(seller_car = mutableListOf(carQashqai,carJuke), purchaser_car = mutableListOf(carMustang), name = "Admin", surname = "Admin", identification = "1234A", birthday = LocalDate.of(2017, 1, 13), email = "admin@admin.com", password = "admin"))
        val user2 = customerService.saveCustomer(Customer(seller_car = mutableListOf(carM3,carS1,carX1,carFocus,carMustang,carMustang2,carFiesta,carFiesta2,carFiesta2), name = "Creditas", surname = "Creditas", identification = "12345A", birthday = LocalDate.of(2017, 1, 13), email = "creditas@creditas.com", password = "creditas"))
        val user3 = customerService.saveCustomer(Customer (name = "Creditas1", surname = "Creditas1", identification = "123456A", birthday = LocalDate.of(2017, 1, 13), email = "creditas1@creditas.com", password = "creditas1"))



    }
}

