package com.InventoryModule.InventoryModule;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;

import com.InventoryModule.InventoryModule.Entity.Inventory;
import com.InventoryModule.InventoryModule.Entity.Product;
import com.InventoryModule.InventoryModule.Entity.Reservation;
import com.InventoryModule.InventoryModule.repo.InventoryRepository;
import com.InventoryModule.InventoryModule.repo.ProductRepository;
import com.InventoryModule.InventoryModule.repo.ReservationRepository;
import com.InventoryModule.InventoryModule.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InventoryServiceTest {

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
    void setup() {
        MockitoAnnotations.openMocks(this);

        testProduct = Product.builder()
                .id(1L)
                .name("Test")
                .description("Testing")
                .price(10.0)
                .build();

        testInventory = Inventory.builder()
                .id(1L)
                .product(testProduct)
                .quantityAvailable(10)
                .build();
    }

    @Test
    void testGetAvailableQuantity() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(testProduct));
        when(inventoryRepo.findByProduct(testProduct)).thenReturn(Optional.of(testInventory));

        int qty = inventoryService.getAvailableQuantity(1L);
        assertEquals(10, qty);
    }

    @Test
    void testReserveItemSuccess() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(testProduct));
        when(inventoryRepo.findByProduct(testProduct)).thenReturn(Optional.of(testInventory));

        boolean result = inventoryService.reserveItem(1L, 5, "RES-1");
        assertTrue(result);
        verify(inventoryRepo).save(any(Inventory.class));
        verify(reservationRepo).save(any(Reservation.class));
    }
}
