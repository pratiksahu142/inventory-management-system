package com.neu.productservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Table(name = "products")
@Entity
@SequenceGenerator(name = "pseq", initialValue = 5, allocationSize = 100000)
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pseq")
  private Integer id;

  @Column(name = "cid")
  private Integer categoryId;

  @Column(name = "name")
  private String name;

  @Column(name = "price")
  private int price;

  @Column(name = "quantity")
  private int quantity;

  @Column(name = "description")
  private String description;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Product{" +
        "productId=" + id +
        ", categoryId=" + categoryId +
        ", name='" + name + '\'' +
        ", price=" + price +
        ", quantity=" + quantity +
        ", description='" + description + '\'' +
        '}';
  }
}
