package com.restaurant;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Map<String, Double> menu = new LinkedHashMap<>();

        // Menu items
        menu.put("Burger", 10.99);
        menu.put("Steak", 19.99);
        menu.put("French Fries", 3.99);
        menu.put("Chicken Wings", 9.99);
        menu.put("Iced Tea", 2.99);
        menu.put("Coffee", 1.99);
        menu.put("Ice Cream", 4.99);

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println();
            System.out.println("================================");
            System.out.println("       RESTAURANT SYSTEM");
            System.out.println("================================");
            System.out.println("1. Check the menu");
            System.out.println("2. Create an order");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            System.out.println();

            switch (choice) {

                case 1:
                    displayMenu(menu);
                    break;

                case 2:
                    createOrder(menu, scanner);
                    break;

                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void displayMenu(Map<String, Double> menu) {

        System.out.println("========== MENU ==========");

        int number = 1;

        for (Map.Entry<String, Double> entry : menu.entrySet()) {

            System.out.printf(
                    "%d. %-20s $%.2f%n",
                    number,
                    entry.getKey(),
                    entry.getValue()
            );

            number++;
        }
    }

    private static void createOrder(
            Map<String, Double> menu,
            Scanner scanner) {

        displayMenu(menu);

        Map<String, Double> selectedProducts =
                new LinkedHashMap<>();

        System.out.print("\nHow many different products do you want? ");
        int numProducts = scanner.nextInt();

        for (int i = 0; i < numProducts; i++) {

            System.out.print(
                    "Enter product number (0 to cancel): ");

            int productNumber = scanner.nextInt();

            if (productNumber == 0) {
                System.out.println("Order cancelled.");
                return;
            }

            String product = getProductName(
                    productNumber,
                    menu
            );

            if (product == null) {
                System.out.println("Invalid product selection.");
                i--;
                continue;
            }

            System.out.print(
                    "Enter quantity of " + product + ": ");

            int quantity = scanner.nextInt();

            if (quantity <= 0) {
                System.out.println("Invalid quantity.");
                i--;
                continue;
            }

            double price = menu.get(product);
            double totalPrice = price * quantity;

            selectedProducts.put(product, totalPrice);
        }

        double totalPrice = 0;

        System.out.println("\n======= YOUR ORDER =======");

        for (Map.Entry<String, Double> entry :
                selectedProducts.entrySet()) {

            System.out.printf(
                    "%-20s $%.2f%n",
                    entry.getKey(),
                    entry.getValue()
            );

            totalPrice += entry.getValue();
        }

        System.out.println("--------------------------");
        System.out.printf(
                "TOTAL:               $%.2f%n",
                totalPrice
        );
    }

    private static String getProductName(
            int number,
            Map<String, Double> menu) {

        if (number < 1 || number > menu.size()) {
            return null;
        }

        int currentNumber = 1;

        for (String product : menu.keySet()) {

            if (currentNumber == number) {
                return product;
            }

            currentNumber++;
        }

        return null;
    }
}