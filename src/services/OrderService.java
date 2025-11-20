package services;

import java.sql.*;
import java.util.Scanner;

import entities.Customer;
import entities.Order;
import entities.Product;
import entities.ProductQuantityPair;

public class OrderService {

    Scanner sc = new Scanner(System.in);

    // PLACE ORDER (FULL JDBC)
    public void placeOrder(ProductService ps, CustomerService cs) {
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = sc.nextInt();

            Customer customer = cs.getCustomerById(customerId);

            if (customer == null) {
                System.out.println("❌ Customer not found!");
                return;
            }

            Connection con = DBConnection.getConnection();

            // 1️⃣ Insert into ORDERS table
            String insertOrderSQL = "INSERT INTO orders (customerId, status) VALUES (?, ?)";
            PreparedStatement orderStmt = con.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);

            orderStmt.setInt(1, customerId);
            orderStmt.setString(2, "Pending"); // default status
            orderStmt.executeUpdate();

            // Get generated orderId
            ResultSet rs = orderStmt.getGeneratedKeys();
            int orderId = 0;

            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            System.out.println("✔ Order Created! Order ID: " + orderId);

            // 2️⃣ Add products to order
            while (true) {
                System.out.print("Enter Product ID (or -1 to finish): ");
                int pid = sc.nextInt();
                if (pid == -1) break;

                Product product = ps.getProductById(pid);
                if (product == null) {
                    System.out.println("❌ Product not found!");
                    continue;
                }

                System.out.print("Enter quantity: ");
                int qty = sc.nextInt();

                if (qty > product.getStockQuantity()) {
                    System.out.println("❌ Not enough stock!");
                    continue;
                }

                // Add to order_items SQL
                String itemSQL = "INSERT INTO order_items(orderId, productId, quantity) VALUES (?, ?, ?)";
                PreparedStatement itemStmt = con.prepareStatement(itemSQL);

                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, pid);
                itemStmt.setInt(3, qty);
                itemStmt.executeUpdate();

                // Reduce product stock in database
                String updateStockSQL = "UPDATE products SET stockQuantity = stockQuantity - ? WHERE productId = ?";
                PreparedStatement stockStmt = con.prepareStatement(updateStockSQL);

                stockStmt.setInt(1, qty);
                stockStmt.setInt(2, pid);
                stockStmt.executeUpdate();

                System.out.println("✔ Added " + qty + "x to order.");
            }

            con.close();
            System.out.println("✔ Order placed successfully and saved to database!");

        } catch (Exception e) {
            System.out.println("❌ Error placing order:");
            e.printStackTrace();
        }
    }

    // VIEW ORDER BY CUSTOMER
    public void viewOrdersByCustomer(int customerId, CustomerService cs) {
        try {
            Customer customer = cs.getCustomerById(customerId);

            if (customer == null) {
                System.out.println("Customer not found!");
                return;
            }

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM orders WHERE customerId=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerId);

            ResultSet ordersRS = ps.executeQuery();

            while (ordersRS.next()) {
                int orderId = ordersRS.getInt("orderId");
                String status = ordersRS.getString("status");

                System.out.println("\nOrder ID: " + orderId + ", Status: " + status);
                System.out.println("Items:");

                // Load order items
                String itemSQL = "SELECT * FROM order_items INNER JOIN products ON order_items.productId = products.productId WHERE orderId=?";
                PreparedStatement itemStmt = con.prepareStatement(itemSQL);
                itemStmt.setInt(1, orderId);

                ResultSet itemRS = itemStmt.executeQuery();

                while (itemRS.next()) {
                    System.out.println("  - " +
                            itemRS.getString("name") +
                            " | Qty: " + itemRS.getInt("quantity"));
                }
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error retrieving customer orders:");
            e.printStackTrace();
        }
    }

    // VIEW ALL ORDERS
    public void viewAllOrders() {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM orders";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n----- ALL ORDERS -----");

            while (rs.next()) {
                int orderId = rs.getInt("orderId");
                int customerId = rs.getInt("customerId");
                String status = rs.getString("status");

                System.out.println("Order ID: " + orderId +
                        ", Customer ID: " + customerId +
                        ", Status: " + status);
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error reading orders:");
            e.printStackTrace();
        }
    }

    // GET ORDER BY ID (used by AdminService)
    public Order getOrderById(int id) {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM orders WHERE orderId=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Create temporary Order object (not fully loaded)
                Order order = new Order(id, null);
                order.setStatus(rs.getString("status"));
                return order;
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
