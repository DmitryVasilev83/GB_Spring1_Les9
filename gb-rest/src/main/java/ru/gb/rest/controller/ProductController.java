package ru.gb.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.gb.rest.dto.ProductDto;
import ru.gb.rest.entity.Cart;
import ru.gb.rest.entity.Product;
import ru.gb.rest.service.ProductService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    Cart cart = new Cart();

    @GetMapping
    public List<Product> getProductList(){
        return productService.findAll();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<? extends Product> getProduct(@PathVariable("productId") Long id){

        if (id != null){
            Product product = productService.findById(id);
            if (product != null){
                return new ResponseEntity<>(product, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> handlePost(@Validated @RequestBody ProductDto productDto){
        ProductDto savedProduct = productService.save(productDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/api/v1/product" + savedProduct.getId()));
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> handleUpdate(@PathVariable("productId") Long id, @Validated @RequestBody ProductDto productDto){
        productDto.setId(id);
        productService.save(productDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("productId") Long id){
        productService.deleteById(id);
    }

    // DZ_9
    @GetMapping("/cart")
    public List<Product> getProductListFromCart(){
        return productService.findAllInCart();
    }

    @GetMapping("/cart/add/{productId}")
    public List<Product> addProductToCart(@PathVariable("productId") Long id){
        productService.addProductToCart(id);
        return productService.findAllInCart();
    }

    @DeleteMapping("/cart/delete/{productId}")
    public List<Product> deleteProductFromCartById(@PathVariable("productId") Long id){
        productService.deleteProductFromCart(id);
        return productService.findAllInCart();
    }


}
