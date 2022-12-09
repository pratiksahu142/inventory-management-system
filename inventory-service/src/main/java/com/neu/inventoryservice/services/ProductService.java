package com.neu.inventoryservice.services;

import com.neu.inventoryservice.model.Product;
import com.neu.inventoryservice.model.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService {

  @Autowired
  RestTemplate restTemplate;

  public Products getAllProducts() {
    return restTemplate.getForObject("http://product-service/product/", Products.class);
  }

  public Product getProductById(Integer productId) {
    return restTemplate.getForObject("http://product-service/product/" + productId, Product.class);
  }

  public Products getProductByName(String name) {
    return restTemplate.getForObject("http://product-service/product/find/" + name, Products.class);
  }

  public Product addProduct(Product product) {
    return restTemplate.postForObject("http://product-service/product/", product, Product.class);
  }

  public void updateProduct(Product product) {
    restTemplate.put("http://product-service/product/" + product.getId(), product);
  }

}
