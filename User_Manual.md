# Restaurant Order and Kitchen Routing Management System

## User Manual

---

## 1. Introduction

The **Restaurant Order and Kitchen Routing Management System** is a Java-based application designed to help restaurant staff manage customer orders and organize the preparation of ordered items in the appropriate kitchen stations.

The system is being developed using:

* Java
* JavaFX
* Maven
* MySQL
* JDBC
* Oracle Cloud (hosting)

The application is intended for restaurant staff such as cashiers, kitchen personnel, and administrators.

---

# 2. Software Requirements

The following software must be installed before running the project.

## 2.1 Java Development Kit (JDK)

The project requires a Java Development Kit.

Recommended:

**JDK 26**

The project is currently configured to use Java 26.

After installing Java, verify the installation by opening a terminal or PowerShell and entering:

```powershell
java -version
```

You should see information showing your installed Java version.

You should also verify the Java compiler:

```powershell
javac -version
```

If both commands return a version number, Java is installed correctly.

---

# 3. Maven

Maven is used to manage the Java project, dependencies, and build process.

Verify Maven by running:

```powershell
mvn -version
```

The command should display information similar to:

```text
Apache Maven
Maven home: ...
Java version: ...
OS: Windows
```

If Maven is not installed, install Apache Maven and add its `bin` directory to the system PATH.

---

# 4. JavaFX

JavaFX is used to create the graphical user interface of the application.

JavaFX does **not** need to be manually downloaded when using the project's Maven configuration.

The required JavaFX dependencies are specified inside:

```text
pom.xml
```

Maven automatically downloads the required JavaFX libraries when the project is built or executed.

---

# 5. Git

Git is recommended for downloading and managing the project source code.

Verify Git by running:

```powershell
git --version
```

Git is useful for keeping the project synchronized with the GitHub repository.

---

# 6. Recommended IDE

The project can be developed using any Java-compatible IDE.

Recommended:

* Visual Studio Code
* IntelliJ IDEA
* Eclipse

This project can be opened using **Visual Studio Code** with the Java extensions installed.

Recommended VS Code extensions:

* Extension Pack for Java
* Maven for Java

---

# 7. Downloading the Project

If the project is hosted on GitHub, clone the repository using:

```powershell
git clone <repository-url>
```

Then enter the project directory:

```powershell
cd Restaurant-order-and-kitchen-routing-system
```

Replace `<repository-url>` with the URL of the project's GitHub repository.

---

# 8. Project Structure

The project follows the standard Maven directory structure.

```text
Restaurant-order-and-kitchen-routing-system/
│
├── pom.xml
├── README.md
├── user_manual.md
│
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── restaurant/
        │           ├── Main.java
        │           │
        │           ├── model/
        │           ├── service/
        │           ├── repository/
        │           └── database/
        │
        └── resources/
            ├── fxml/
            └── css/
```

### Important files

| File/Folder   | Purpose                                      |
| ------------- | -------------------------------------------- |
| `pom.xml`     | Maven project configuration and dependencies |
| `Main.java`   | Main application entry point                 |
| `model/`      | Classes representing restaurant data         |
| `service/`    | Application and business logic               |
| `repository/` | Database-related operations                  |
| `database/`   | Database connection code                     |
| `resources/`  | FXML, CSS, images, and other resources       |

---

# 9. Installing Project Dependencies

After opening the project in the terminal, run:

```powershell
mvn clean install
```

Maven will:

1. Read the `pom.xml` file.
2. Download required dependencies.
3. Compile the Java source code.
4. Build the project.
5. Report any compilation errors.

The first build may take longer because Maven needs to download dependencies.

---

# 10. Running the Application

To start the JavaFX application, run:

```powershell
mvn javafx:run
```

Alternatively:

```powershell
mvn clean javafx:run
```

The second command cleans the previous build before starting the application.

If the configuration is correct, the **Restaurant Order System** window should appear.

---

# 11. Current Application

The current version of the application provides a graphical restaurant ordering interface.

The menu currently contains:

| Menu Item     |  Price |
| ------------- | -----: |
| Burger        | $10.99 |
| Steak         | $19.99 |
| French Fries  |  $3.99 |
| Chicken Wings |  $9.99 |
| Iced Tea      |  $2.99 |
| Coffee        |  $1.99 |
| Ice Cream     |  $4.99 |

---

# 12. Creating an Order

To create an order:

### Step 1

Launch the application:

```powershell
mvn javafx:run
```

### Step 2

Select an item from the menu.

For example:

```text
Burger - $10.99
```

### Step 3

Click the menu item again if another quantity is required.

For example, clicking Burger twice results in:

```text
Burger x 2 = $21.98
```

### Step 4

Continue selecting other menu items.

Example:

```text
Burger x 2 = $21.98
French Fries x 1 = $3.99
Iced Tea x 1 = $2.99
```

### Step 5

The application automatically calculates the order total.

Example:

```text
Total: $28.96
```

---

# 13. Clearing an Order

To remove all currently selected items:

1. Click the **Clear Order** button.
2. The current order will be cleared.
3. The total will return to:

```text
Total: $0.00
```

---

# 14. Kitchen Routing

The planned kitchen routing functionality will automatically assign ordered items to the appropriate kitchen station.

Example:

```text
Burger
    ↓
Grill Station

French Fries
    ↓
Fry Station

Iced Tea
    ↓
Beverage Station

Ice Cream
    ↓
Dessert Station
```

A complete order may therefore be divided between multiple kitchen stations.

Example:

```text
ORDER #1024

GRILL
├── Burger x2
└── Steak x1

FRY
└── French Fries x1

BEVERAGE
└── Iced Tea x1

DESSERT
└── Ice Cream x1
```

This allows kitchen personnel to focus on the items assigned to their station.

---

# 15. User Roles

The planned system contains three primary user roles.

## Administrator

The administrator manages system configuration.

Possible functions:

* Manage menu items
* Manage kitchen stations
* Manage staff accounts
* View reports
* Manage system settings

---

## Cashier / Order Staff

The cashier handles customer orders.

Possible functions:

* Log in
* View menu
* Create orders
* Modify orders
* Confirm orders
* View order status
* Cancel orders

---

## Kitchen Staff

Kitchen personnel handle food preparation.

Possible functions:

* Log in
* View assigned order items
* View kitchen queue
* Update preparation status
* Mark items as ready

---

# 16. Order Workflow

The planned workflow is:

```text
Customer
    │
    ▼
Cashier
    │
    ▼
Create Order
    │
    ▼
Confirm Order
    │
    ▼
Restaurant System
    │
    ▼
Kitchen Routing
    │
    ├──────────────┬──────────────┬──────────────┐
    ▼              ▼              ▼              ▼
  Grill           Fry         Beverage       Dessert
    │              │              │              │
    └──────────────┴──────────────┴──────────────┘
                           │
                           ▼
                    Order Completed
                           │
                           ▼
                         Served
```

---

# 17. Database

The planned system will use **MySQL** as its database.

The database will store information such as:

* Menu items
* Prices
* Kitchen stations
* Customer orders
* Order details
* Staff accounts
* Order statuses

The Java application will communicate with MySQL through **JDBC**.

The planned architecture is:

```text
JavaFX GUI
     │
     ▼
Service Layer
     │
     ▼
Repository Layer
     │
     ▼
JDBC
     │
     ▼
MySQL Database
```

---

# 18. Troubleshooting

## Java command is not recognized

If PowerShell displays an error such as:

```text
'java' is not recognized...
```

Java may not be installed correctly or may not have been added to the system PATH.

Verify:

```powershell
java -version
```

---

## Maven command is not recognized

Verify Maven:

```powershell
mvn -version
```

If Maven is not recognized, check that Maven's `bin` directory has been added to the system PATH.

---

## JavaFX application does not start

First try:

```powershell
mvn clean
```

Then:

```powershell
mvn javafx:run
```

If the problem continues, check the error message displayed in the terminal.

---

## Dependencies are missing

Run:

```powershell
mvn clean install
```

Maven should automatically download the dependencies specified in `pom.xml`.

---

## Compilation errors

Run:

```powershell
mvn clean compile
```

Read the error displayed in the terminal.

The error will usually indicate:

* The affected Java file
* The line number
* The type of compilation error

---

# 19. Updating the Project

If the project is connected to GitHub, retrieve the latest version using:

```powershell
git pull
```

After making changes:

```powershell
git add .
git commit -m "Update restaurant system"
git push
```

---

# 20. Development Commands

Frequently used Maven commands:

### Compile the project

```powershell
mvn compile
```

### Clean previous builds

```powershell
mvn clean
```

### Build the project

```powershell
mvn package
```

### Clean and build

```powershell
mvn clean install
```

### Run JavaFX

```powershell
mvn javafx:run
```

### Clean and run JavaFX

```powershell
mvn clean javafx:run
```

---

# 21. Development Notes

The application is currently under development.

The development process is divided into several stages:

### Stage 1 — Project Setup

* Java
* Maven
* JavaFX
* Git/GitHub

### Stage 2 — GUI

* Login interface
* Dashboard
* Order interface
* Kitchen interface
* Administrator interface

### Stage 3 — Application Logic

* Menu management
* Order management
* Order calculations
* Kitchen routing
* Order status tracking

### Stage 4 — Database

* MySQL
* JDBC
* Database tables
* CRUD operations

### Stage 5 — Testing

* Functional testing
* Usability testing
* Routing accuracy
* System response time
* Reliability testing

---

# 22. Important Information

This system is intended as an academic/project application and is designed around the restaurant workflow defined by the project requirements.

The exact features available may change as development continues.

Always check the project's `README.md` and source code for the most current implementation.

---

# 23. Quick Start

For users who already have Java and Maven installed:

```powershell
git clone <repository-url>

cd Restaurant-order-and-kitchen-routing-system

mvn clean install

mvn javafx:run
```

The application should then open in a JavaFX window.

---

# 24. Summary

The Restaurant Order and Kitchen Routing Management System uses JavaFX to provide a graphical interface for restaurant staff.

The system is designed to:

* Manage restaurant orders
* Organize menu items
* Automatically route items to kitchen stations
* Allow kitchen staff to manage preparation
* Track order status
* Store restaurant information in a centralized database

The main technologies used are:

```text
Java
  │
  ├── JavaFX → Graphical User Interface
  │
  ├── Maven → Project & Dependency Management
  │
  ├── JDBC → Database Communication
  │
  └── MySQL → Data Storage
```

The goal is to provide an organized system that connects restaurant order taking with kitchen preparation and routing.
