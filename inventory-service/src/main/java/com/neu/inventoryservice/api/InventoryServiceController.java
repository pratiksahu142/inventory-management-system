package com.neu.inventoryservice.api;

import com.neu.inventoryservice.dao.InventoryLookupRepository;
import com.neu.inventoryservice.dao.InventoryRepository;
import com.neu.inventoryservice.dao.UserRepository;
import com.neu.inventoryservice.model.Categories;
import com.neu.inventoryservice.model.Category;
import com.neu.inventoryservice.model.FullProduct;
import com.neu.inventoryservice.model.Inventory;
import com.neu.inventoryservice.model.InventoryLookup;
import com.neu.inventoryservice.model.Product;
import com.neu.inventoryservice.model.Products;
import com.neu.inventoryservice.model.User;
import com.neu.inventoryservice.services.CategoryService;
import com.neu.inventoryservice.services.ProductService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
  UserRepository userRepository;

  @Autowired
  ProductService productService;

  @Autowired
  CategoryService categoryService;

  @PostMapping("/login")
  public ResponseEntity login(@RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    Optional<User> optUser = userRepository.findById(username);
    if (optUser.isPresent()) {
      User user = optUser.get();
      if (!user.getPassword().equals(password)) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      user.setLoggedIn(true);
      userRepository.save(user);
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @PostMapping("/logout")
  public ResponseEntity logout(@RequestHeader("username") String username) {
    Optional<User> optUser = userRepository.findById(username);
    if (optUser.isPresent()) {
      User user = optUser.get();
      user.setLoggedIn(false);
      userRepository.save(user);
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @PostMapping("/")
  public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory,
      @RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    try {
      boolean isUserLoggedIn = isUserLoggedIn(username, password);
      if (!isUserLoggedIn) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
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
      @PathVariable("inventoryId") Integer inventoryId,
      @RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    boolean isUserLoggedIn = isUserLoggedIn(username, password);
    if (!isUserLoggedIn) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);
    return inventory.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @RequestMapping("/")
  public ResponseEntity<List<Inventory>> getAllInventories(
      @RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    boolean isUserLoggedIn = isUserLoggedIn(username, password);
    if (!isUserLoggedIn) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    List<Inventory> inventories = new ArrayList<>();

    inventoryRepository.findAll().forEach(inventories::add);
    if (inventories.size() > 0) {
      return new ResponseEntity<>(inventories, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{inventoryId}")
  public ResponseEntity<HttpStatus> deleteInventory(
      @PathVariable("inventoryId") Integer inventoryId,
      @RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    try {
      boolean isUserLoggedIn = isUserLoggedIn(username, password);
      if (!isUserLoggedIn) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      inventoryRepository.deleteById(inventoryId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{inventoryId}/products/{productId}")
  public ResponseEntity<InventoryLookup> deleteProductFromInventory(
      @PathVariable("inventoryId") Integer inventoryId,
      @PathVariable("productId") Integer productId,
      @RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    boolean isUserLoggedIn = isUserLoggedIn(username, password);
    if (!isUserLoggedIn) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);
    if (inventoryOptional.isPresent()) {
      List<InventoryLookup> inventoryLookups = inventoryLookupRepository.findInventoryLookupsByInventoryIdEquals(
          inventoryId);
      for (InventoryLookup inventoryLookup : inventoryLookups) {
        if (inventoryLookup.getProductId().equals(productId)) {
          Product product = productService.getProductById(productId);
          product.setQuantity(product.getQuantity() + inventoryLookup.getQuantity());
          productService.updateProduct(product);
          inventoryLookupRepository.deleteById(inventoryLookup.getId());
          return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
      }
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PutMapping("/{inventoryId}/products/{productId}")
  public ResponseEntity<InventoryLookup> removeProductFromInventory(
      @PathVariable("inventoryId") Integer inventoryId,
      @PathVariable("productId") Integer productId,
      @RequestHeader("username") String username,
      @RequestHeader("password") String password,
      @RequestBody InventoryLookup reqInventoryLookup) {
    boolean isUserLoggedIn = isUserLoggedIn(username, password);
    if (!isUserLoggedIn) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);
    if (inventoryOptional.isPresent()) {
      List<InventoryLookup> inventoryLookups = inventoryLookupRepository.findInventoryLookupsByInventoryIdEquals(
          inventoryId);
      for (InventoryLookup inventoryLookup : inventoryLookups) {
        if (inventoryLookup.getProductId().equals(productId)) {
          inventoryLookup.setQuantity(
              inventoryLookup.getQuantity() - reqInventoryLookup.getQuantity());
          if (inventoryLookup.getQuantity() < 1) {
            inventoryLookupRepository.deleteById(inventoryLookup.getId());
          } else {
            inventoryLookupRepository.save(inventoryLookup);
          }
          Product product = productService.getProductById(productId);
          product.setQuantity(product.getQuantity() + reqInventoryLookup.getQuantity());
          productService.updateProduct(product);
          return new ResponseEntity<>(inventoryLookupRepository.save(inventoryLookup),
              HttpStatus.ACCEPTED);
        }
      }
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PostMapping("/{inventoryId}/products/")
  public ResponseEntity<InventoryLookup> addProductToInventory(
      @PathVariable("inventoryId") Integer inventoryId,
      @RequestBody InventoryLookup inventoryLookup,
      @RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    boolean isUserLoggedIn = isUserLoggedIn(username, password);
    if (!isUserLoggedIn) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);
    List<InventoryLookup> inventoryLUs = inventoryLookupRepository.findInventoryLookupsByInventoryIdEquals(
        inventoryId);
    InventoryLookup inventoryLookupFromDb = null;
    for (InventoryLookup inventoryLookup1 : inventoryLUs) {
      if (inventoryLookup1.getProductId().equals(inventoryLookup.getProductId())) {
        inventoryLookupFromDb = inventoryLookup1;
        break;
      }
    }
    if (inventoryOptional.isPresent()) {
      Product product = productService.getProductById(inventoryLookup.getProductId());
      if (product != null && product.getQuantity() >= inventoryLookup.getQuantity()) {
        product.setQuantity(product.getQuantity() - inventoryLookup.getQuantity());
        productService.updateProduct(product);
        InventoryLookup newInventory;
        if (inventoryLookupFromDb != null) {
          newInventory = inventoryLookupRepository
              .save(InventoryLookup.builder()
                  .id(inventoryLookupFromDb.getId())
                  .inventoryId(inventoryId)
                  .productId(inventoryLookup.getProductId())
                  .quantity(inventoryLookup.getQuantity() + inventoryLookupFromDb.getQuantity())
                  .build());
        } else {
          newInventory = inventoryLookupRepository
              .save(InventoryLookup.builder()
                  .inventoryId(inventoryId)
                  .productId(inventoryLookup.getProductId())
                  .quantity(inventoryLookup.getQuantity())
                  .build());
        }

        return new ResponseEntity<>(newInventory, HttpStatus.CREATED);
      } else {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping("/{inventoryId}/products")
  public ResponseEntity<List<FullProduct>> getAllProductsInInventory(
      @PathVariable("inventoryId") Integer inventoryId,
      @RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    boolean isUserLoggedIn = isUserLoggedIn(username, password);
    if (!isUserLoggedIn) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);
    if (inventoryOptional.isPresent()) {
      List<InventoryLookup> inventoryLookups =
          inventoryLookupRepository.findInventoryLookupsByInventoryIdEquals(inventoryId);
      Map<Product, Integer> productsInInventory = new HashMap<>();
      for (InventoryLookup inventoryLookup : inventoryLookups) {
        productsInInventory.put(productService.getProductById(inventoryLookup.getProductId()),
            inventoryLookup.getQuantity());
      }
      List<Category> categories = categoryService.getAllCategories().getCategories();
      Map<Integer, String> categoriesMap = new HashMap<>();
      for (Category category : categories) {
        categoriesMap.put(category.getId(), category.getName());
      }
      List<FullProduct> fullProducts = new ArrayList<>();
      for (Map.Entry<Product, Integer> productInInventory : productsInInventory.entrySet()) {
        Product product = productInInventory.getKey();
        fullProducts.add(FullProduct.builder()
            .iid(inventoryId)
            .pid(product.getId())
            .cid(product.getCategoryId())
            .productName(product.getName())
            .inventoryName(inventoryOptional.get().getName())
            .categoryName(categoriesMap.get(product.getCategoryId()))
            .quantity(productInInventory.getValue())
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
  public ResponseEntity<Product> addProduct(@RequestBody Product product,
      @RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    try {
      boolean isUserLoggedIn = isUserLoggedIn(username, password);
      if (!isUserLoggedIn) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      Product newProduct = new Product();
      newProduct.setId(null);
      newProduct.setName(product.getName());
      newProduct.setCategoryId(product.getCategoryId());
      newProduct.setPrice(product.getPrice());
      newProduct.setDescription(product.getDescription());
      newProduct = productService.addProduct(newProduct);
      return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/categories/")
  public ResponseEntity<Category> addCategory(@RequestBody Category category,
      @RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    try {
      boolean isUserLoggedIn = isUserLoggedIn(username, password);
      if (!isUserLoggedIn) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      Category newCategory = new Category();
      newCategory.setName(category.getName());
      newCategory = categoryService.addCategory(newCategory);
      return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping("/category/")
  public ResponseEntity<Categories> getAllCategories(@RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    boolean isUserLoggedIn = isUserLoggedIn(username, password);
    if (!isUserLoggedIn) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    List<Category> categories = new ArrayList<>(categoryService.getAllCategories().getCategories());
    if (categories.size() > 0) {
      return new ResponseEntity<>(new Categories(categories), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping("/products/")
  public ResponseEntity<Products> getAllProducts(@RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    boolean isUserLoggedIn = isUserLoggedIn(username, password);
    if (!isUserLoggedIn) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    List<Product> products = new ArrayList<>(productService.getAllProducts().getProducts());
    if (products.size() > 0) {
      return new ResponseEntity<>(new Products(products), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping("/products/{productId}")
  public ResponseEntity<Product> getProduct(@PathVariable("productId") Integer productId,
      @RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    boolean isUserLoggedIn = isUserLoggedIn(username, password);
    if (!isUserLoggedIn) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
  }

  @PutMapping("/products/{productId}")
  public ResponseEntity<Product> updateProduct(@PathVariable("productId") Integer productId,
      @RequestBody Product product, @RequestHeader("username") String username,
      @RequestHeader("password") String password) {
    boolean isUserLoggedIn = isUserLoggedIn(username, password);
    if (!isUserLoggedIn) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    product.setId(productId);
    productService.updateProduct(product);
    return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
  }

  private boolean isUserLoggedIn(String username, String password) {
    Optional<User> optUser = userRepository.findById(username);
    if (optUser.isPresent()) {
      User user = optUser.get();
      return user.getPassword().equals(password) && user.isLoggedIn();
    }
    return false;
  }
}
