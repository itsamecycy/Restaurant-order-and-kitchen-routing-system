# Restaurant Order and Kitchen Routing Management System

A Java-based restaurant management system designed to streamline the processing, routing, and monitoring of customer orders within a restaurant's kitchen operations.

## 📌 Project Overview

The **Restaurant Order and Kitchen Routing Management System** is a proposed software solution that helps restaurants manage customer orders and efficiently route individual order items to their appropriate kitchen stations.

Unlike a traditional Point-of-Sale (POS) system that primarily focuses on transactions and payment processing, this system focuses on the **operational workflow of restaurant orders**, particularly the process of assigning orders to kitchen stations and monitoring their preparation status.

For example, when a customer places an order containing:

* Burger
* French Fries
* Iced Tea

The system can automatically route the items to:

```text
Burger       → Grill Station
French Fries → Fry Station
Iced Tea     → Beverage Station
```

This allows kitchen staff to focus on their assigned preparation tasks while reducing manual order routing and improving kitchen workflow.

---

## 🎯 Objectives

### General Objective

To design and develop a restaurant order and kitchen routing management system that improves the organization and efficiency of restaurant order processing and kitchen operations.

### Specific Objectives

The system aims to:

1. Manage customer orders efficiently.
2. Maintain and manage restaurant menu items.
3. Automatically route ordered items to their appropriate kitchen stations.
4. Allow kitchen staff to view assigned orders.
5. Monitor the preparation status of individual order items.
6. Manage kitchen station availability and workload.
7. Provide an organized queue for kitchen orders.
8. Store and retrieve order records using a database.
9. Generate basic reports related to restaurant orders and operations.

---

## 🔄 System Workflow

```text
                 CUSTOMER
                    │
                    ▼
              Create Order
                    │
                    ▼
            Order Processing
                    │
                    ▼
            Kitchen Routing
                    │
       ┌────────────┼────────────┐
       ▼            ▼            ▼
     GRILL          FRY       BEVERAGE
    STATION       STATION      STATION
       │            │            │
       ▼            ▼            ▼
    Burger        Fries       Iced Tea
       │            │            │
       └────────────┼────────────┘
                    ▼
             Order Completed
                    │
                    ▼
                  SERVED
```

---

## ✨ Main Features

### 1. Order Management

Users can:

* Create new orders
* Add menu items
* Specify quantities
* Remove items from an order
* Calculate the total order amount
* Confirm orders
* View existing orders

### 2. Automatic Kitchen Routing

The system determines which kitchen station should handle each ordered item.

Example:

```text
Menu Item       Kitchen Station
--------------------------------
Burger          Grill
Steak           Grill
French Fries    Fry
Chicken Wings   Fry
Iced Tea        Beverage
Coffee          Beverage
Ice Cream       Dessert
```

### 3. Kitchen Station Management

The system supports different kitchen stations, such as:

* Grill Station
* Fry Station
* Beverage Station
* Dessert Station

Each station can view the order items assigned to it.

### 4. Order Status Tracking

Orders can progress through different statuses:

```text
PENDING
   ↓
CONFIRMED
   ↓
PREPARING
   ↓
READY
   ↓
SERVED
```

Orders may also be marked as:

```text
CANCELLED
```

### 5. Kitchen Queue

Each kitchen station can have its own queue of incoming orders.

Example:

```text
GRILL STATION
-------------------------
Order #1025
Burger x2
Status: PREPARING

Order #1026
Steak x1
Status: PENDING

Order #1027
Burger x1
Status: PENDING
```

### 6. Menu Management

Authorized users can manage:

* Menu item names
* Prices
* Categories
* Assigned kitchen stations
* Item availability

### 7. Database Management

The system stores information about:

* Menu items
* Orders
* Order items
* Kitchen stations
* Staff/users
* Order statuses

---

<!--# 🏗️ Project Architecture

The project follows a layered architecture inspired by the **MVC (Model-View-Controller)** design pattern.

```text
RestaurantKitchenSystem/
│
├── src/
│   └── main/
│       └── java/
│           └── com.restaurant/
│
│               ├── Main.java
│               │
│               ├── model/
│               │   ├── MenuItem.java
│               │   ├── Order.java
│               │   ├── OrderItem.java
│               │   ├── KitchenStation.java
│               │   ├── Staff.java
│               │   └── OrderStatus.java
│               │
│               ├── service/
│               │   ├── OrderService.java
│               │   ├── KitchenRoutingService.java
│               │   ├── MenuService.java
│               │   └── ReportService.java
│               │
│               ├── repository/
│               │   ├── OrderRepository.java
│               │   ├── MenuRepository.java
│               │   └── StationRepository.java
│               │
│               ├── database/
│               │   └── DatabaseConnection.java
│               │
│               ├── ui/
│               │   ├── MainMenu.java
│               │   ├── OrderScreen.java
│               │   ├── KitchenScreen.java
│               │   └── AdminScreen.java
│               │
│               └── util/
│                   ├── InputValidator.java
│                   └── DateTimeUtil.java
│
├── resources/
│   ├── config.properties
│   └── schema.sql
│
├── README.md
└── pom.xml
```

---
This structure is still in progress-->

<!--
# 🗄️ Database Structure

The system uses a relational database to store restaurant and order information.

Main tables include:

### `stations`

Stores information about kitchen stations.

| Field        | Description               |
| ------------ | ------------------------- |
| `station_id` | Unique station identifier |
| `name`       | Kitchen station name      |
| `status`     | Current station status    |

### `menu_items`

Stores restaurant menu items.

| Field        | Description                 |
| ------------ | --------------------------- |
| `item_id`    | Unique menu item identifier |
| `name`       | Name of the food/drink      |
| `price`      | Item price                  |
| `category`   | Menu category               |
| `station_id` | Assigned kitchen station    |

### `orders`

Stores customer orders.

| Field        | Description             |
| ------------ | ----------------------- |
| `order_id`   | Unique order identifier |
| `order_date` | Date and time of order  |
| `total`      | Total order amount      |
| `status`     | Current order status    |

### `order_items`

Stores individual items belonging to an order.

| Field           | Description                  |
| --------------- | ---------------------------- |
| `order_item_id` | Unique order item identifier |
| `order_id`      | Associated order             |
| `item_id`       | Associated menu item         |
| `quantity`      | Quantity ordered             |
| `status`        | Preparation status           |

---
THERE IS NO DATABASE YET-->
<!--
# 🛠️ Technologies Used

| Technology | Purpose                           |
| ---------- | --------------------------------- |
| **Java**   | Main programming language         |
| **JavaFX** | Graphical user interface          |
| **MySQL**  | Database management               |
| **JDBC**   | Java-to-database connection       |
| **Maven**  | Dependency and project management |
| **Git**    | Version control                   |
| **GitHub** | Source code collaboration         |

---

# 📋 Requirements

Before running the project, make sure you have:

* Java JDK 17 or later
* Maven
* MySQL Server
* MySQL Workbench (optional)
* Git
* An IDE such as IntelliJ IDEA, Eclipse, or VS Code

---
TO IMPLEMENT-->
<!--# 🚀 Installation

## 1. Clone the Repository

```bash
git clone <repository-url>
```

Navigate into the project:

```bash
cd RestaurantKitchenSystem
```

## 2. Create the Database

Create a MySQL database:

```sql
CREATE DATABASE restaurant_system;
```

Then execute the SQL schema located in:

```text
resources/schema.sql
```

## 3. Configure the Database

Update the database configuration in:

```text
resources/config.properties
```

Example:

```properties
db.url=jdbc:mysql://localhost:3306/restaurant_system
db.username=root
db.password=your_password
```

**Do not commit your actual database password to GitHub.**

## 4. Install Dependencies

Using Maven:

```bash
mvn clean install
```

## 5. Run the Application

Run the `Main.java` class from your IDE, or use the appropriate Maven command configured for the project.

---
Not ready for use yet-->

# 👥 User Roles

The system may support multiple user roles.

### Administrator

Responsible for:

* Managing menu items
* Managing kitchen stations
* Managing staff accounts
* Viewing reports

### Order Staff

Responsible for:

* Creating customer orders
* Updating orders
* Viewing order information

### Kitchen Staff

Responsible for:

* Viewing assigned kitchen orders
* Updating preparation status
* Marking items as ready

---

# 📊 Example Scenario

A customer places the following order:

```text
Order #1025

2x Burger
1x French Fries
2x Iced Tea
```

The system processes the order:

```text
                 Order #1025
                      │
          ┌───────────┼───────────┐
          ▼           ▼           ▼
       Burger       Fries       Iced Tea
          │           │           │
          ▼           ▼           ▼
        GRILL         FRY       BEVERAGE
       STATION      STATION      STATION
```

Each kitchen station receives only the items that it needs to prepare.

Once preparation is complete:

```text
Grill       → READY
Fry         → READY
Beverage    → READY
                │
                ▼
          Order Completed
                │
                ▼
              SERVED
```

---

# 🆚 Difference From a POS System

A traditional POS system primarily focuses on:

```text
Customer Order
      ↓
Payment
      ↓
Receipt
      ↓
Sales Record
```

This project focuses on:

```text
Customer Order
      ↓
Order Processing
      ↓
Automatic Kitchen Routing
      ↓
Kitchen Station Assignment
      ↓
Food Preparation
      ↓
Order Status Tracking
```

The system can therefore complement a POS system by focusing on the **back-of-house kitchen workflow**.

---

# 🎓 Research/Thesis Relevance

The project aims to address potential problems in manual restaurant kitchen workflows, including:

* Miscommunication between order staff and kitchen staff
* Incorrect routing of food orders
* Delays caused by manual order distribution
* Difficulty tracking order preparation
* Unorganized kitchen queues
* Lack of visibility into kitchen workload

The system provides an automated approach to order routing and kitchen workflow management.

---

# 🔮 Future Improvements

Potential future versions may include:

* Real-time kitchen displays
* Kitchen workload balancing
* Estimated preparation times
* Order priority management
* Inventory management
* Customer-facing order tracking
* QR-code ordering
* Integration with POS systems
* Sales and performance analytics
* Mobile application support
* Cloud-based database
* Multi-branch restaurant support

---

# 📌 Project Status

**Development Status:** In Development

This project is being developed as an academic/thesis project using Java.

---

# 👨‍💻 Developers

Developed by:

**[Squad MaChamp]**

* [Auman, Pale John]
* [Bayquen, Cyrus]
* [Cosio, Janine]
* [FariÑas, Prince Jewel]
* [Larracas, Ruben]
* [Mariano, Sarah]
* [Reyes, Angela]
* [Viñas, Tisha Lyan]
* []

**Course:** [BS Computer Engr.]
**Year Level:** 3rd Year
**Institution:** [ICCT Colleges]
**Academic Year:** 2026–2027

---

# 📄 License

This project is developed for academic and educational purposes.
