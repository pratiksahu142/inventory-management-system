package com.neu.inventoryservice.model;

import java.util.List;

public class Products {

  List<Product> products;

  public Products(List<Product> products) {
    this.products = products;
  }

  public Products() {
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }
}
