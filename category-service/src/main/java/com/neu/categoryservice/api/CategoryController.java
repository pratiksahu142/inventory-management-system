package com.neu.categoryservice.api;

import com.neu.categoryservice.dao.CategoryRepository;
import com.neu.categoryservice.model.Category;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping("/category")
public class CategoryController {

  @Autowired
  CategoryRepository categoryRepository;

  @RequestMapping("/{categoryId}")
  public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Integer categoryId) {
    Optional<Category> category = categoryRepository.findById(categoryId);
    return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping("/")
  public ResponseEntity<Category> addCategory(@RequestBody Category category) {
    try {
      Category newCategory = categoryRepository
          .save(Category.builder().name(category.getName()).build());
      return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping("/")
  public ResponseEntity<List<Category>> getAllCategories() {
    Iterable<Category> categoryIterator = categoryRepository.findAll();
    List<Category> categories = new ArrayList<>();
    categoryIterator.forEach(categories::add);
    if (categories.size() > 0) {
      return new ResponseEntity<>(categories, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<HttpStatus> deleteCategory(@PathVariable("categoryId") Integer categoryId) {
    try {
      categoryRepository.deleteById(categoryId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
