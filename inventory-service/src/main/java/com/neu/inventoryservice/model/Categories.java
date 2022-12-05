package com.neu.inventoryservice.model;

import java.util.List;

public class Categories {

  private List<Category> categories;

  public Categories() {
  }

  public Categories(List<Category> categories) {
    this.categories = categories;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }
}
