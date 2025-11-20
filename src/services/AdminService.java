package services;

import java.sql.*;
import java.util.Scanner;

import entities.Admin;
import entities.Order;

public class AdminService {

    Scanner sc = new Scanner(System.in);
    private OrderService orderService;

    public AdminService(OrderService orderService) {
        this.orderService = orderService;
    }

    // ADD ADMIN (INSERT INTO DATABASE)
    public void createAdmin() {
        try {
            System.out.print("Enter Admin ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Username: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO admins(adminId, username, email) VALUES(?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, email);

            ps.executeUpdate();
            con.close();

            System.out.println("✔ Admin created successfully and saved to database!");

        } catch (Exception e) {
            System.out.println("❌ Error creating admin:");
            e.printStackTrace();
        }
    }

    // VIEW ADMINS FROM DATABASE
    public void viewAdmins() {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM admins";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n----- ADMIN LIST -----");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                    "Admin ID: " + rs.getInt("adminId") +
                    " | Username: " + rs.getString("username") +
                    " | Email: " + rs.getString("email")
                );
            }

            if (!found)
                System.out.println("❌ No admins found in database.");

            con.close();

        } catch (Exception e) {
            System.out.println("❌ Error reading admins:");
            e.printStackTrace();
        }
    }

    // UPDATE ORDER STATUS IN DATABASE
    public void updateOrderStatus() {
        try {
            System.out.print("Enter Order ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            // Check if order exists using JDBC
            Order order = orderService.getOrderById(id);

            if (order == null) {
                System.out.println("❌ Order not found in database!");
                return;
            }

            System.out.print("Enter new status (Completed/Delivered/Cancelled): ");
            String status = sc.nextLine();

            Connection con = DBConnection.getConnection();
            String sql = "UPDATE orders SET status=? WHERE orderId=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, status);
            ps.setInt(2, id);

            ps.executeUpdate();
            con.close();

            System.out.println("✔ Order status updated in database!");

        } catch (Exception e) {
            System.out.println("❌ Error updating order status:");
            e.printStackTrace();
        }
    }
}
