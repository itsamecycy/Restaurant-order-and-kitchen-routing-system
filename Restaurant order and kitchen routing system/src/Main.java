import java.util.Scanner;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        HashMap<String, Double> menu = new HashMap<String, Double>();

        //Menu Items
        menu.put("Burger", 10.99);
        menu.put("Steak", 20.99);
        menu.put("French Fries", 3.99);
        menu.put("Chicken Wings", 4.99);
        menu.put("Iced Tea", 5.99);
        menu.put("Coffee", 3.99);
        menu.put("Ice Cream", 5.99);

        while(true){
            System.out.println("1. View Menu");
            System.out.println("2. Create Order");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = input.nextInt();

            switch(choice){
                case 1:
                    System.out.println("Menu:");
                    for (String item : menu.keySet()) {
                        System.out.println(item + " - $" + menu.get(item));
                    }
                    break;
                case 2:
                    System.out.println("Create Order");
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

}
