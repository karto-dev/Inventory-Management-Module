package com.InventoryModule.InventoryModule;

import com.InventoryModule.InventoryModule.Entity.Inventory;
import com.InventoryModule.InventoryModule.Entity.Product;
import com.InventoryModule.InventoryModule.Entity.Reservation;
import com.InventoryModule.InventoryModule.repo.InventoryRepository;
import com.InventoryModule.InventoryModule.repo.ProductRepository;
import com.InventoryModule.InventoryModule.repo.ReservationRepository;
import com.InventoryModule.InventoryModule.service.InventoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private ProductRepository productRepo;

    @Mock
    private InventoryRepository inventoryRepo;

    @Mock
    private ReservationRepository reservationRepo;

    private Product testProduct;
    private Inventory testInventory;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Desc")
                .price(100.0)
                .build();

        testInventory = Inventory.builder()
                .id(1L)
                .product(testProduct)
                .quantityAvailable(10)
                .build();
    }

    @Test
    void testGetAvailableQuantity_Success() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(testProduct));
        when(inventoryRepo.findByProduct(any())).thenReturn(Optional.of(testInventory));

        int result = inventoryService.getAvailableQuantity(1L);

        assertEquals(10, result);
        verify(productRepo).findById(1L);
        verify(inventoryRepo).findByProduct(any());
    }

    @Test
    void testGetAvailableQuantity_ProductNotFound() {
        when(productRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventoryService.getAvailableQuantity(1L);
        });

        assertTrue(exception.getMessage().contains("Product not found"));
    }

    @Test
    void testReserveItem_Success() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(testProduct));
        when(inventoryRepo.findByProduct(any())).thenReturn(Optional.of(testInventory));
        when(inventoryRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(reservationRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        boolean reserved = inventoryService.reserveItem(1L, 5, "RES-1001");

        assertTrue(reserved);
        assertEquals(5, testInventory.getQuantityAvailable());
        verify(inventoryRepo).save(any());
        verify(reservationRepo).save(any());
    }

    @Test
    void testReserveItem_InsufficientQuantity() {
        testInventory.setQuantityAvailable(3);

        when(productRepo.findById(1L)).thenReturn(Optional.of(testProduct));
        when(inventoryRepo.findByProduct(any())).thenReturn(Optional.of(testInventory));

        boolean result = inventoryService.reserveItem(1L, 5, "RES-1002");

        assertFalse(result);
        verify(reservationRepo, never()).save(any());
    }

    @Test
    void testReserveItem_ProductNotFound() {
        when(productRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventoryService.reserveItem(1L, 5, "RES-1003"));

        assertTrue(exception.getMessage().contains("Product not found"));
        verify(reservationRepo, never()).save(any());
    }

    @Test
    void testCreateOrUpdateInventory_NewProductInventory() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(testProduct));
        when(inventoryRepo.findByProduct(testProduct)).thenReturn(Optional.empty());

        Inventory saved = Inventory.builder()
                .id(1L)
                .product(testProduct)
                .quantityAvailable(10)
                .build();

        when(inventoryRepo.save(any(Inventory.class))).thenReturn(saved);

        Inventory result = inventoryService.createOrUpdateInventory(1L, 10);

        assertEquals(10, result.getQuantityAvailable());
        verify(inventoryRepo).save(any(Inventory.class));
    }
    @Test
    void testCancelReservation_Success() {
        Reservation reservation = Reservation.builder()
                .id(1L)
                .product(testProduct)
                .quantity(5)
                .reservationReference("RES-123")
                .active(true)
                .build();

        testInventory.setQuantityAvailable(10);

        when(reservationRepo.findByReservationReference("RES-123")).thenReturn(List.of(reservation));
        when(inventoryRepo.findByProduct(testProduct)).thenReturn(Optional.of(testInventory));
        when(inventoryRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(reservationRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        boolean result = inventoryService.cancelReservation("RES-123");

        assertTrue(result);
        assertEquals(15, testInventory.getQuantityAvailable()); // 10 + 5
        assertFalse(reservation.isActive());
    }


}
