package com.example.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String URL = "jdbc:mysql://localhost:3306/vehicle_rental_system";
    private static final String USER = "root";
    private static final String PASSWORD = "Kayweed@01*";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void insertVehicle(HelloApplication.Vehicle v) {
        String sql = "INSERT INTO vehicles (id, brand, model, category, ratePerDay, status, imagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, v.id);
            stmt.setString(2, v.brand);
            stmt.setString(3, v.model);
            stmt.setString(4, v.category);
            stmt.setDouble(5, v.ratePerDay);
            stmt.setString(6, v.status);
            stmt.setString(7, v.imagePath);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<HelloApplication.Vehicle> getAllVehicles() {
        List<HelloApplication.Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(new HelloApplication.Vehicle(
                        rs.getString("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("category"),
                        rs.getDouble("ratePerDay"),
                        rs.getString("status"),
                        rs.getString("imagePath")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public static void deleteVehicle(String vehicleId) {
        String sql = "DELETE FROM vehicles WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vehicleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateVehicle(HelloApplication.Vehicle v) {
        String sql = "UPDATE vehicles SET brand=?, model=?, category=?, ratePerDay=?, status=?, imagePath=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, v.brand);
            stmt.setString(2, v.model);
            stmt.setString(3, v.category);
            stmt.setDouble(4, v.ratePerDay);
            stmt.setString(5, v.status);
            stmt.setString(6, v.imagePath);
            stmt.setString(7, v.id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
