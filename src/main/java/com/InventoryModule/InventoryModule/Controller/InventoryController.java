package com.InventoryModule.InventoryModule.Controller;

import com.InventoryModule.InventoryModule.Entity.Inventory;
import com.InventoryModule.InventoryModule.Entity.Product;
import com.InventoryModule.InventoryModule.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(inventoryService.createProduct(product));
    }

    @PostMapping("/supply")
    public ResponseEntity<Inventory> addSupply(
            @RequestParam Long productId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.createOrUpdateInventory(productId, quantity));
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveItem(
            @RequestParam Long productId,
            @RequestParam int quantity,
            @RequestParam String ref) {
        boolean success = inventoryService.reserveItem(productId, quantity, ref);
        return success ?
                ResponseEntity.ok("Reserved") :
                ResponseEntity.badRequest().body("Not enough stock");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelReservation(@RequestParam String ref) {
        boolean cancelled = inventoryService.cancelReservation(ref);
        return cancelled ?
                ResponseEntity.ok("Cancelled") :
                ResponseEntity.badRequest().body("Reservation not found");
    }

    @GetMapping("/available")
    public ResponseEntity<Integer> getAvailable(@RequestParam Long productId) {
        return ResponseEntity.ok(inventoryService.getAvailableQuantity(productId));
    }
}
