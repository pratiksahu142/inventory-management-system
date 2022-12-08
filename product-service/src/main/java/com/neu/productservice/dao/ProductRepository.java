package com.neu.productservice.dao;

import com.neu.productservice.model.Product;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {

//  List<Product> findByInventoryId(Integer inventoryId);
  List<Product> findAllByName(String name);
}
