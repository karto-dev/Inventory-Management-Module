package com.InventoryModule.InventoryModule.repo;

import com.InventoryModule.InventoryModule.Entity.Product;
import com.InventoryModule.InventoryModule.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByProductAndActiveTrue(Product product);
    List<Reservation> findByReservationReference(String reference);
}
