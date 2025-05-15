package org.example.appwarehouse.controller;

import org.example.appwarehouse.entity.Product;
import org.example.appwarehouse.payload.ProductDto;
import org.example.appwarehouse.payload.ProductResponseDto;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping
    public Result addProduct(@RequestBody ProductDto productDto) {
        Result result = productService.addProduct(productDto);
        return result;
    };
    @PutMapping("/{id}")
    public Result updateProduct(@PathVariable Integer id, @RequestBody ProductDto productDto) {
        Result result = productService.updateProduct(id, productDto);
        return result;
    }

    @GetMapping("/list")
    public List<ProductResponseDto> getProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return products;
    };

    @GetMapping("/{id}")
    public ProductResponseDto getProduct(@PathVariable Integer id) {
        ProductResponseDto product = productService.getProductById(id);
        return product;
    }

    @DeleteMapping("/{id}")
    public Result deleteProduct(@PathVariable Integer id) {
        Result result = productService.deleteProduct(id);
        return result;
    }
}
