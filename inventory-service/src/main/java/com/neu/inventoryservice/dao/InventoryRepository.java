package com.neu.inventoryservice.dao;

import com.neu.inventoryservice.model.Inventory;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<Inventory, Integer> {

}
