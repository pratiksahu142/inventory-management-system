package com.neu.inventoryservice.dao;

import com.neu.inventoryservice.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

}
