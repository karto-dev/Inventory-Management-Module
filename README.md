# 🛒 E-Commerce Inventory Management Module

This project is a core inventory backend module built using Java and Spring Boot. It serves as the foundation for managing product stock, reservations, and concurrency handling in an e-commerce system.

## 🚀 Features Implemented

### ✅ Inventory Operations
- **Create Supply**: Add or update stock for a product
- **Reserve Inventory**: Lock items temporarily for orders
- **Cancel Reservations**: Rollback unused reservations
- **Availability Check**: Real-time stock query
- **Safe Concurrency**: Handles simultaneous requests safely

### ⚙️ Tech Stack
- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **MySQL** (Relational DB)
- **Redis** (High-speed caching)
- **Maven**
- **Postman** for API testing
- **JUnit + Mockito** for unit tests
- **Swagger** for API documentation

## 🛠️ REST API Endpoints

| Method | Endpoint                    | Description                         |
|--------|-----------------------------|-------------------------------------|
| POST   | `/api/inventory/supply`    | Add or update supply                |
| POST   | `/api/inventory/reserve`   | Reserve product quantity            |
| POST   | `/api/inventory/cancel`    | Cancel a reservation                |
| GET    | `/api/inventory/available` | Get current available quantity      |
| GET    | `/swagger-ui.html`         | Swagger docs (auto-generated)       |

## 💾 Data Persistence

- All data is stored in a MySQL database (`ecommerce_db`)
- Entities auto-mapped via JPA annotations
- Redis is used to cache `availableQuantity` lookups

##  Testing

- Unit tests using `JUnit 5` and `Mockito`
- Test coverage for:
  - Successful reservations
  - Failure due to no stock
  - Product not found scenarios
- Redis integration tested with local instance

## 📂 Project Structure

com.InventoryModule.InventoryModule
├── controller
├── service
├── entity
├── repo
└── test


## 📦 How to Run

1. Clone the project:

2. Set up MySQL and Redis locally

3. Run the Spring Boot app:

4. Visit Swagger UI: http://localhost:8080/swagger-ui.html

---

## 🧠 Future Enhancements

- Add JWT-based authentication
- Order module integration
- Coupon and billing modules
- Dockerize the app and Redis


