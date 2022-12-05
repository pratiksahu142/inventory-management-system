package com.neu.inventoryservice.model;

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
@Table(name = "inventorylookup")
@Entity
@SequenceGenerator(name = "ilseq", initialValue = 5, allocationSize = 1000)
public class InventoryLookup {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ilseq")
  private Integer id;

  @Column(name = "iid")
  private Integer inventoryId;

  @Column(name = "pid")
  private Integer productId;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getInventoryId() {
    return inventoryId;
  }

  public void setInventoryId(Integer inventoryId) {
    this.inventoryId = inventoryId;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  @Override
  public String toString() {
    return "InventoryLookup{" +
        "id=" + id +
        ", inventoryId=" + inventoryId +
        ", productId=" + productId +
        '}';
  }
}
