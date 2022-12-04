package com.neu.productservice.api;

import com.neu.productservice.dao.ProductRepository;
import com.neu.productservice.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  ProductRepository productRepository;

  @RequestMapping("/{productId}")
  public ResponseEntity<Product> getProductById(@PathVariable("productId") int productId) {
    Optional<Product> product = productRepository.findById(productId);
    return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @RequestMapping("/")
  public ResponseEntity<List<Product>> getAllProducts() {
    Iterable<Product> productIterator = productRepository.findAll();
    List<Product> products = new ArrayList<>();
    productIterator.forEach(products::add);
    if (products.size() > 0) {
      return new ResponseEntity<>(products, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/")
  public ResponseEntity<Product> addCategory(@RequestBody Product product) {
    try {
      Product newProduct = productRepository
          .save(new Product());
      return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
