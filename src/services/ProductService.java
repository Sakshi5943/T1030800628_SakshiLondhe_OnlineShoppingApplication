package services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.Product;

public class ProductService {

    Scanner sc = new Scanner(System.in);

    // Add Product (INSERT into MySQL)
    public void addProduct() {
        try {
            System.out.print("Enter Product ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Product Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Product Price: ");
            double price = sc.nextDouble();

            System.out.print("Enter Stock Quantity: ");
            int qty = sc.nextInt();

            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO products(productId, name, price, stockQuantity) VALUES(?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setDouble(3, price);
            ps.setInt(4, qty);

            ps.executeUpdate();
            con.close();

            System.out.println("\n✔ Product inserted into MySQL successfully!\n");

        } catch (Exception e) {
            System.out.println("❌ Error inserting product:");
            e.printStackTrace();
        }
    }

    // Remove Product (DELETE from MySQL)
    public void removeProduct() {
        try {
            System.out.print("Enter Product ID to remove: ");
            int id = sc.nextInt();

            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM products WHERE productId=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            con.close();

            if (rows > 0)
                System.out.println("✔ Product removed successfully!");
            else
                System.out.println("❌ Product not found in database.");

        } catch (Exception e) {
            System.out.println("Error removing product:");
            e.printStackTrace();
        }
    }

    // View All Products (SELECT from MySQL)
    public void viewProducts() {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM products";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n----- PRODUCT LIST -----");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println(
                    "Product ID: " + rs.getInt("productId") +
                    " | Name: " + rs.getString("name") +
                    " | Price: " + rs.getDouble("price") +
                    " | Stock: " + rs.getInt("stockQuantity")
                );
            }

            if (!found)
                System.out.println("No products available!");

            con.close();

        } catch (Exception e) {
            System.out.println("Error reading products:");
            e.printStackTrace();
        }
    }

    // Get product by ID (SELECT WHERE)
    public Product getProductById(int id) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM products WHERE productId=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("productId"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stockQuantity")
                );
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error fetching product:");
            e.printStackTrace();
        }

        return null;
    }

    // Get list of products
    public List<Product> getProductList() {
        List<Product> productList = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM products";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                productList.add(
                    new Product(
                        rs.getInt("productId"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stockQuantity")
                    )
                );
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error loading product list:");
            e.printStackTrace();
        }

        return productList;
    }
}
