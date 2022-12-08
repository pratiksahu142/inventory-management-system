package com.neu.categoryservice.dao;

import com.neu.categoryservice.model.Category;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

  List<Category> findAllByName(String name);
}
