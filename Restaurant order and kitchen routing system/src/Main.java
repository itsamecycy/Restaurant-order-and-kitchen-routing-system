import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Hello may I have your Order?: ");
        System.out.println("Order: ");
        String order = input.nextLine();

        input.close();
    }
}
