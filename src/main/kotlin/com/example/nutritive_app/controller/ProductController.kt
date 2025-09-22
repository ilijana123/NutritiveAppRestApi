package com.example.nutritive_app.controller

import com.example.nutritive_app.dto.ProductDTO
import com.example.nutritive_app.dto.ProductDetailsDTO
import com.example.nutritive_app.dto.request.CreateProductRequest
import com.example.nutritive_app.service.ProductService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(): List<ProductDTO> = productService.getAllProducts()

    @GetMapping("/{id}")
    fun getProductsById(@PathVariable("id") barcode: String): ProductDetailsDTO =
        productService.getProductsById(barcode)

    @PostMapping
    fun createProduct(@RequestBody dto: ProductDTO): ProductDTO =
        productService.createProduct(dto)

    @PutMapping("/{id}")
    fun updateProductById(@PathVariable("id") barcode: String, @RequestBody dto: ProductDTO): ProductDTO =
        productService.updateProductById(barcode, dto)

    @DeleteMapping("/{id}")
    fun deleteProductsById(@PathVariable("id") barcode: String) =
        productService.deleteProductsById(barcode)

    @PostMapping("/add")
    fun addProduct(@RequestBody req: CreateProductRequest): ProductDTO {
        return productService.createSimpleProduct(req)
    }
}
