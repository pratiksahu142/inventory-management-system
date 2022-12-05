package com.neu.productservice.api;

import com.neu.productservice.dao.ProductRepository;
import com.neu.productservice.model.Product;
import com.neu.productservice.model.Products;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  public ResponseEntity<Products> getAllProducts() {
    List<Product> products = new ArrayList<>();
    productRepository.findAll().forEach(products::add);
    if (products.size() > 0) {
      return new ResponseEntity<>(new Products(products), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

//  @RequestMapping("/inventory/{inventoryId}")
//  public ResponseEntity<Products> getAllInventoryProducts(
//      @PathVariable("inventoryId") int inventoryId) {
//    List<Product> products = new ArrayList<>();
//    productRepository.findByInventoryId(inventoryId).forEach(products::add);
//    if (products.size() > 0) {
//      return new ResponseEntity<>(new Products(products), HttpStatus.OK);
//    } else {
//      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//  }

  @PostMapping("/")
  public ResponseEntity<Product> addProduct(@RequestBody Product product) {
    try {
      Product newProduct = productRepository
          .save(Product.builder()
              .name(product.getName())
              .categoryId(product.getCategoryId())
              .price(product.getPrice())
              .quantity(product.getQuantity())
              .description(product.getDescription())
              .build());
      return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{productId}")
  public ResponseEntity<Product> updateProduct(@PathVariable("productId") Integer productId,
      @RequestBody Product product) {
    Optional<Product> productOptional = productRepository.findById(productId);

    if (productOptional.isPresent()) {
      Product oldProduct = productOptional.get();
      oldProduct.setName(product.getName());
      oldProduct.setPrice(product.getPrice());
      oldProduct.setCategoryId(product.getCategoryId());
      oldProduct.setQuantity(product.getQuantity());
      oldProduct.setDescription(product.getDescription());
      return new ResponseEntity<>(productRepository.save(oldProduct), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/category/{productId}")
  public ResponseEntity<Product> updateProductCategory(@PathVariable("productId") Integer productId,
      @RequestBody Product product) {
    Optional<Product> productOptional = productRepository.findById(productId);

    if (productOptional.isPresent()) {
      Product oldProduct = productOptional.get();
      oldProduct.setCategoryId(product.getCategoryId());
      return new ResponseEntity<>(productRepository.save(oldProduct), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<HttpStatus> deleteProductById(
      @PathVariable("productId") Integer productId) {
    try {
      productRepository.deleteById(productId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
