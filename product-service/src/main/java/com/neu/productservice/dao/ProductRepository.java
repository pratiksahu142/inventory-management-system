package com.neu.productservice.dao;

import com.neu.productservice.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {

//  List<Product> findByInventoryId(Integer inventoryId);
}
