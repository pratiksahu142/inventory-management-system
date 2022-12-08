package com.neu.inventoryservice.services;

import com.neu.inventoryservice.model.Categories;
import com.neu.inventoryservice.model.Category;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CategoryService {

  private static final String CATEGORY_SERVICE = "categoryService";

  @Autowired
  RestTemplate restTemplate;

  @CircuitBreaker(name = CATEGORY_SERVICE, fallbackMethod = "getFallbackCategory")
  public Categories getAllCategories() {
    return restTemplate.getForObject("http://category-service/category/", Categories.class);
  }

  public Categories getAllCategoriesByName(String name) {
    return restTemplate.getForObject("http://category-service/category/find/"+name, Categories.class);
  }


  public Categories getFallbackCategory(Throwable t) {
    System.out.println("*******Category Fallback Activated*******");
    List<Category> categoryList = new ArrayList<>();
    for (int i = 1; i < 1000; i++) {
      categoryList.add(new Category(i, "Default Category"));
    }
    return new Categories(categoryList);
  }

  public Category addCategory(Category newCategory) {
    return restTemplate.postForObject("http://category-service/category/", newCategory,
        Category.class);
  }
}
