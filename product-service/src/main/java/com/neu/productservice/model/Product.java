package com.neu.productservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Product {

  @GeneratedValue
  @Id
  @Column(name = "pid")
  private int productId;

  @Column(name = "cid")
  private int categoryId;

  @Column(name = "name")
  private String name;

  @Column(name = "price")
  private int price;

  @Column(name = "quantity")
  private int quantity;

  @Column(name = "description")
  private String description;

  @Override
  public String toString() {
    return "Product{" +
        "productId=" + productId +
        ", categoryId=" + categoryId +
        ", name='" + name + '\'' +
        ", price=" + price +
        ", quantity=" + quantity +
        ", description='" + description + '\'' +
        '}';
  }
}
