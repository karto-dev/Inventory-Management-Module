package com.InventoryModule.InventoryModule.service;

import com.InventoryModule.InventoryModule.Entity.Inventory;
import com.InventoryModule.InventoryModule.Entity.Product;
import com.InventoryModule.InventoryModule.Entity.Reservation;
import com.InventoryModule.InventoryModule.repo.InventoryRepository;
import com.InventoryModule.InventoryModule.repo.ProductRepository;
import com.InventoryModule.InventoryModule.repo.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepo;
    private final InventoryRepository inventoryRepo;
    private final ReservationRepository reservationRepo;

    @Cacheable(value = "inventory", key = "#productId")
    public int getAvailableQuantity(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow();
        Inventory inventory = inventoryRepo.findByProduct(product).orElseThrow();
        return inventory.getQuantityAvailable();
    }

    @CacheEvict(value = "inventory", key = "#productId")
    public Inventory createOrUpdateInventory(Long productId, int quantity) {
        Product product = productRepo.findById(productId).orElseThrow();
        Optional<Inventory> optional = inventoryRepo.findByProduct(product);
        Inventory inventory = optional.orElse(new Inventory(null, product, 0));
        inventory.setQuantityAvailable(inventory.getQuantityAvailable() + quantity);
        return inventoryRepo.save(inventory);
    }

    @CacheEvict(value = "inventory", key = "#productId")
    public synchronized boolean reserveItem(Long productId, int quantity, String reservationRef) {
        Product product = productRepo.findById(productId).orElseThrow();
        Inventory inventory = inventoryRepo.findByProduct(product).orElseThrow();

        if (inventory.getQuantityAvailable() < quantity) return false;

        inventory.setQuantityAvailable(inventory.getQuantityAvailable() - quantity);
        inventoryRepo.save(inventory);

        Reservation reservation = Reservation.builder()
                .product(product)
                .quantity(quantity)
                .reservationReference(reservationRef)
                .active(true)
                .build();

        reservationRepo.save(reservation);
        return true;
    }


    public Product createProduct(Product product) {
        return productRepo.save(product);
    }


    @Transactional
    public boolean cancelReservation(String reservationRef) {
        List<Reservation> reservations = reservationRepo.findByReservationReference(reservationRef);
        if (reservations.isEmpty()) return false;

        for (Reservation r : reservations) {
            if (r.isActive()) {
                Inventory inventory = inventoryRepo.findByProduct(r.getProduct()).orElseThrow();
                inventory.setQuantityAvailable(inventory.getQuantityAvailable() + r.getQuantity());
                inventoryRepo.save(inventory);
                r.setActive(false);
                reservationRepo.save(r);
            }
        }
        return true;
    }

}
