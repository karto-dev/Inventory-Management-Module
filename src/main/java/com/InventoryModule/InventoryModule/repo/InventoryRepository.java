package com.InventoryModule.InventoryModule.repo;

import com.InventoryModule.InventoryModule.Entity.Inventory;
import com.InventoryModule.InventoryModule.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProduct(Product product);
}
