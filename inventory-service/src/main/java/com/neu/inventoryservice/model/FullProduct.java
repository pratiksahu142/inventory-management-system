package com.neu.inventoryservice.model;

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
public class FullProduct {

  private String categoryName;

  private String inventoryName;

  private String productName;

  private int price;

  private int quantity;

  private String description;

}
