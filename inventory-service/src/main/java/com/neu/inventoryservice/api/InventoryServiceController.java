package com.neu.inventoryservice.api;

import com.neu.inventoryservice.dao.InventoryLookupRepository;
import com.neu.inventoryservice.dao.InventoryRepository;
import com.neu.inventoryservice.model.Category;
import com.neu.inventoryservice.model.FullProduct;
import com.neu.inventoryservice.model.Inventory;
import com.neu.inventoryservice.model.InventoryLookup;
import com.neu.inventoryservice.model.Product;
import com.neu.inventoryservice.services.CategoryService;
import com.neu.inventoryservice.services.ProductService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryServiceController {

  @Autowired
  InventoryRepository inventoryRepository;

  @Autowired
  InventoryLookupRepository inventoryLookupRepository;

  @Autowired
  ProductService productService;

  @Autowired
  CategoryService categoryService;

  @PostMapping("/")
  public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory) {
    try {
      Inventory newInventory = inventoryRepository
          .save(Inventory.builder()
              .name(inventory.getName())
              .build());
      return new ResponseEntity<>(newInventory, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping("/{inventoryId}")
  public ResponseEntity<Inventory> getInventoryById(
      @PathVariable("inventoryId") Integer inventoryId) {
    Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);
    return inventory.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{inventoryId}")
  public ResponseEntity<HttpStatus> deleteInventory(
      @PathVariable("inventoryId") Integer inventoryId) {
    try {
      inventoryRepository.deleteById(inventoryId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{inventoryId}/products/{productId}")
  public ResponseEntity<InventoryLookup> deleteProductFromInventory(
      @PathVariable("inventoryId") Integer inventoryId,
      @PathVariable("productId") Integer productId) {
    Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);
    if (inventoryOptional.isPresent()) {
      List<InventoryLookup> inventoryLookups = inventoryLookupRepository.findInventoryLookupsByInventoryIdEquals(
          inventoryId);
      for (InventoryLookup inventoryLookup : inventoryLookups) {
        if (inventoryLookup.getProductId().equals(productId)) {
          inventoryLookupRepository.deleteById(inventoryLookup.getId());
          return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
      }
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PostMapping("/{inventoryId}/products/")
  public ResponseEntity<InventoryLookup> addProductToInventory(
      @PathVariable("inventoryId") Integer inventoryId,
      @RequestBody InventoryLookup inventoryLookup) {
    Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);
    if (inventoryOptional.isPresent()) {
      InventoryLookup newInventory = inventoryLookupRepository
          .save(InventoryLookup.builder()
              .inventoryId(inventoryId)
              .productId(inventoryLookup.getProductId())
              .build());
      return new ResponseEntity<>(newInventory, HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping("/{inventoryId}/products")
  public ResponseEntity<List<FullProduct>> getAllProductsInInventory(
      @PathVariable("inventoryId") Integer inventoryId) {
    Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);
    if (inventoryOptional.isPresent()) {
      List<InventoryLookup> inventoryLookups =
          inventoryLookupRepository.findInventoryLookupsByInventoryIdEquals(inventoryId);
      List<Product> productsInInventory = new ArrayList<>();
      for (InventoryLookup inventoryLookup : inventoryLookups) {
        productsInInventory.add(productService.getProductById(inventoryLookup.getProductId()));
      }
      List<Category> categories = categoryService.getAllCategories().getCategories();
      Map<Integer, String> categoriesMap = new HashMap<>();
      for (Category category : categories) {
        categoriesMap.put(category.getId(), category.getName());
      }
      List<FullProduct> fullProducts = new ArrayList<>();
      for (Product product : productsInInventory) {
        fullProducts.add(FullProduct.builder()
            .productName(product.getName())
            .inventoryName(inventoryOptional.get().getName())
            .categoryName(categoriesMap.get(product.getCategoryId()))
            .quantity(product.getQuantity())
            .price(product.getPrice())
            .description(product.getDescription())
            .build());
      }
      return new ResponseEntity<>(fullProducts, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/products/")
  public ResponseEntity<Product> addProduct(@RequestBody Product product) {
    try {
      Product newProduct = new Product();
      newProduct.setId(null);
      newProduct.setName(product.getName());
      newProduct.setCategoryId(product.getCategoryId());
      newProduct.setPrice(product.getPrice());
      newProduct.setQuantity(product.getQuantity());
      newProduct.setDescription(product.getDescription());
      newProduct = productService.addProduct(newProduct);
      return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/categories/")
  public ResponseEntity<Category> addCategory(@RequestBody Category category) {
    try {
      Category newCategory = new Category();
      newCategory.setName(category.getName());
      newCategory = categoryService.addCategory(newCategory);
      return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
