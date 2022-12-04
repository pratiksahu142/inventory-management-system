package com.neu.categoryservice.dao;

import com.neu.categoryservice.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
}
