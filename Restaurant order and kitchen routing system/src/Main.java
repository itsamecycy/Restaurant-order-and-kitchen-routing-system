import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Map<String, Double> menu = new HashMap<>();

        // Add menu items with prices
        menu.put("Burger", 10.99);
        menu.put("Steak", 19.99);
        menu.put("French Fries", 3.99);
        menu.put("Chicken Wings", 9.99);
        menu.put("Iced Tea", 2.99);
        menu.put("Coffee", 1.99);
        menu.put("Ice Cream", 4.99);

        Scanner scanner = new Scanner(System.in);

        // Prompt the user for the number of products they want
        System.out.print("Enter the number of products you want: ");
        int numProducts = scanner.nextInt();

        // Prompt the user for the product selections
        System.out.println("Select products by entering their corresponding numbers:");
        for (int i = 1; i <= menu.size(); i++) {
            String product = getProductName(i, menu);
            System.out.println(i + ". " + product);
        }

        // Store the product selections
        Map<String, Integer> selectedProducts = new HashMap<>();

        for (int i = 0; i < numProducts; i++) {
            System.out.print("Enter the number of the product you want (or 0 to exit): ");
            int productNumber = scanner.nextInt();

            if (productNumber == 0) {
                break;
            }

            String product = getProductName(productNumber, menu);
            System.out.print("Enter the quantity of " + product + ": ");
            int quantity = scanner.nextInt();

            if (menu.containsKey(product)) {
                double price = menu.get(product);
                double totalPrice = price * quantity;
                selectedProducts.put(product, (int) totalPrice);
            } else {
                System.out.println("Invalid product selection");
            }
        }

        // Calculate the total price
        double totalPrice = 0;
        for (double price : selectedProducts.values()) {
            totalPrice += price;
        }

        // Print the selected products and their total prices
        System.out.println("Selected products and their total prices:");
        for (Map.Entry<String, Integer> entry : selectedProducts.entrySet()) {
            String product = entry.getKey();
            int total = entry.getValue();
            System.out.println(product + ": $" + total);
        }

        System.out.println("Total price: $" + totalPrice);
    }

    private static String getProductName(int number, Map<String, Double> menu) {
        for (Map.Entry<String, Double> entry : menu.entrySet()) {
            if (number == 1) {
                return entry.getKey();
            }
            number--;
        }
        return "Invalid product selection";
    }
}