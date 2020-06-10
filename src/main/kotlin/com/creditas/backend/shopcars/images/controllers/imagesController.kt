package com.creditas.backend.shopcars.images.controllers

import org.apache.juli.logging.LogFactory
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.random.Random
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@RestController
@CrossOrigin
@RequestMapping("api/v1/images")
class imagesController() {
    val fileBasePath = "src/main/resources/images/"
    private val LOGGER = LogFactory.getLog("ImageController.class")



    @PostMapping("/upload")
    fun uploadCarImage(@RequestParam file: MultipartFile): ResponseEntity<String> {
        val random = Random.nextInt(0,1000)
        file.getOriginalFilename()?.apply() {
            val fileName: String = random.toString() + StringUtils.cleanPath(this)
            val path: Path = Paths.get(fileBasePath + fileName);
            try {
                Files.copy(file.inputStream, path, StandardCopyOption.REPLACE_EXISTING)
            } catch (e: Exception) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Fallo al subir imagen")
            }
            val fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/images/get/")
                    .path(fileName)
                    .toUriString()
            return ResponseEntity.status(HttpStatus.OK).body(fileDownloadUri)
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Fallo al subir imagen")
    }

    @GetMapping("/get/{image}")
    fun getImage(@PathVariable image: String): ResponseEntity<Resource>? {
        val rootLocation = Paths.get(fileBasePath).resolve(image)
        LOGGER.warn(rootLocation.toUri())
        val resource = UrlResource(rootLocation.toUri())

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}