package com.InventoryModule.InventoryModule.repo;

import com.InventoryModule.InventoryModule.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
