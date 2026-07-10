import java.util.Scanner;

public class SmartInventory {
    public static void main(String[] args) {
        // 1. Storage: Simple items and their stock numbers
        String[] products = { "Laptop", "Smart Phone", "Wireless Mouse" };
        int[] stock = { 5, 12, 2 };
        int lowStockThreshold = 4; // Any stock 4 or below triggers a warning

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Smart Inventory Tracking Utility ===");

        // 2. The App Loop (Keeps running until you choose to exit)
        while (running) {
            System.out.println("\n--- Current Stock Status ---");
            for (int i = 0; i < products.length; i++) {
                String alert = "";
                if (stock[i] <= lowStockThreshold) {
                    alert = " [⚠️ LOW STOCK]";
                }
                System.out.println("[" + i + "] " + products[i] + ": " + stock[i] + " units" + alert);
            }

            // 3. Simple Menu Options
            System.out.println("\nOptions: [1] Update Stock | [2] Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter item number (0-2): ");
                int index = scanner.nextInt();

                if (index >= 0 && index < products.length) {
                    System.out.print("Enter new quantity for " + products[index] + ": ");
                    int newQty = scanner.nextInt();

                    stock[index] = newQty; // Update the stock list
                    System.out.println("✅ Stock updated successfully!");
                } else {
                    System.out.println("❌ Invalid item number!");
                }
            } else if (choice == 2) {
                running = false;
                System.out.println("Exiting system. Goodbye!");
            } else {
                System.out.println("❌ Invalid option. Try again.");
            }
        }
        scanner.close();
    }
}