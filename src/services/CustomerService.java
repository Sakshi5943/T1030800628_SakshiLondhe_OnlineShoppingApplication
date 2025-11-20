package services;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.Customer;

public class CustomerService{

    Scanner sc = new Scanner(System.in);

    // Create new customer (INSERT into MySQL)
    public void createCustomer() {
        try {
            System.out.print("Enter User ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Username: ");
            String username = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Address: ");
            String address = sc.nextLine();

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO customers(userId, username, email, address) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, address);

            ps.executeUpdate();
            con.close();

            System.out.println("Customer inserted into MySQL successfully!");

        } catch (Exception e) {
            System.out.println("Error inserting customer:");
            e.printStackTrace();
        }
    }

    // View all customers (SELECT from MySQL)
    public void viewCustomers() {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM customers";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("Customers:");

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        "User ID: " + rs.getInt("userId") +
                        ", Username: " + rs.getString("username") +
                        ", Email: " + rs.getString("email") +
                        ", Address: " + rs.getString("address")
                );
            }

            if (!found) {
                System.out.println("No customers found!");
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error reading customers:");
            e.printStackTrace();
        }
    }

    // Get customer by ID
    public Customer getCustomerById(int id) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM customers WHERE userId=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("address")
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
