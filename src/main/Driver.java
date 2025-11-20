package main;

import java.util.Scanner;
import java.sql.*;
import services.DBConnection;
import services.AdminService;
import services.CustomerService;
import services.OrderService;
import services.ProductService;

public class Driver {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ProductService ps = new ProductService();
        CustomerService cs = new CustomerService();
        OrderService os = new OrderService();
        AdminService as = new AdminService(os);

        while (true) {
            System.out.println("\n1. Admin Menu");
            System.out.println("2. Customer Menu");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            if (choice == 1) adminMenu(ps, as, os, sc);
            else if (choice == 2) customerMenu(ps, cs, os, sc);
            else break;
        }

        sc.close();
        System.out.println("Exiting...");
    }

    public static void adminMenu(ProductService ps, AdminService as, OrderService os, Scanner sc) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Product");
            System.out.println("2. Remove Product");
            System.out.println("3. View Products");
            System.out.println("4. Create Admin");
            System.out.println("5. View Admins");
            System.out.println("6. Update Order Status");
            System.out.println("7. View Orders");
            System.out.println("8. Return");
            System.out.print("Choose an option: ");
            int opt = sc.nextInt();

            switch (opt) {
                case 1 -> ps.addProduct();
                case 2 -> ps.removeProduct();
                case 3 -> ps.viewProducts();
                case 4 -> as.createAdmin();
                case 5 -> as.viewAdmins();
                case 6 -> as.updateOrderStatus(os);
                case 7 -> os.viewAllOrders();
                case 8 -> { return; }
            }
        }
    }

    public static void customerMenu(ProductService ps, CustomerService cs, OrderService os, Scanner sc) {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Create Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Place Order");
            System.out.println("4. View Orders");
            System.out.println("5. View Products");
            System.out.println("6. Return");
            System.out.print("Choose an option: ");
            int opt = sc.nextInt();

            switch (opt) {
                case 1 -> cs.createCustomer();
                case 2 -> cs.viewCustomers();
                case 3 -> os.placeOrder(ps, cs);
                case 4 -> {
                    System.out.print("Enter Customer ID: ");
                    int id = sc.nextInt();
                    os.viewOrdersByCustomer(id, cs);
                }
                case 5 -> ps.viewProducts();
                case 6 -> { return; }
            }
        }
    }
}
