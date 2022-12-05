package com.neu.inventoryservice.dao;

import com.neu.inventoryservice.model.InventoryLookup;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface InventoryLookupRepository extends CrudRepository<InventoryLookup, Integer> {

  List<InventoryLookup> findInventoryLookupsByInventoryIdEquals(Integer inventoryId);
}
