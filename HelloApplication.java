package com.example.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    static class Vehicle {
        String id, brand, model, category, status;
        double ratePerDay;
        String imagePath;
        Vehicle(String id, String brand, String model, String category, double ratePerDay, String status, String imagePath) {
            this.id = id;
            this.brand = brand;
            this.model = model;
            this.category = category;
            this.ratePerDay = ratePerDay;
            this.status = status;
            this.imagePath = imagePath;
        }
        @Override public String toString() {
            return id + " - " + brand + " " + model + " [" + category + "] (" + status + ")";
        }
    }

    static class Customer {
        String name, contact, license;
        String rentalHistory = "";
        Customer(String name, String contact, String license) {
            this.name = name;
            this.contact = contact;
            this.license = license;
        }
        @Override public String toString() { return name + " [" + license + "]"; }
    }

    static class User {
        String username, role;
        User(String username, String role) {
            this.username = username;
            this.role = role;
        }
    }

    private final ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private User loggedInUser;
    private BorderPane root = new BorderPane();
    private VBox vehicleSection, customerSection, bookingSection, dashboardSection;

    @Override
    public void start(Stage stage) {
        showLogin(stage);
    }

    private void showLogin(Stage stage) {
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            if (username.equals("admin") && password.equals("admin123")) {
                loggedInUser = new User("admin", "Admin");
                showMainApp(stage);
            } else if (username.equals("employee") && password.equals("emp123")) {
                loggedInUser = new User("employee", "Employee");
                showMainApp(stage);
            } else {
                showAlert("Login Failed", "Invalid credentials!");
            }
        });

        VBox loginBox = new VBox(10, userLabel, userField, passLabel, passField, loginButton);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(100));
        loginBox.setStyle("-fx-background-color: silver; -fx-border-radius: 10; -fx-background-radius: 10;");

        Scene scene = new Scene(loginBox, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Login - Vehicle Rental System");
        stage.show();
    }

    private void showMainApp(Stage stage) {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #1a1a1a;");
        Label logo = new Label("🚗 CAR RENT");
        logo.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        Button dashboard = new Button("Dashboard");
        Button vehicleManagement = new Button("Vehicle Management");
        Button customerManagement = new Button("Customer Management");
        Button bookingSystem = new Button("Booking System");
        Button paymentBilling = new Button("Payment & Billing");
        Button reportsVisualization = new Button("Reports & Data Visualization");
        Button logoutButton = new Button("Logout");

        dashboard.setMaxWidth(Double.MAX_VALUE);
        vehicleManagement.setMaxWidth(Double.MAX_VALUE);
        customerManagement.setMaxWidth(Double.MAX_VALUE);
        bookingSystem.setMaxWidth(Double.MAX_VALUE);
        paymentBilling.setMaxWidth(Double.MAX_VALUE);
        reportsVisualization.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setMaxWidth(Double.MAX_VALUE);

        createVehicleSection();
        createCustomerSection();
        createBookingSection();
        createDashboard();

        vehicleManagement.setOnAction(e -> root.setCenter(vehicleSection));
        customerManagement.setOnAction(e -> root.setCenter(customerSection));
        bookingSystem.setOnAction(e -> root.setCenter(bookingSection));
        paymentBilling.setOnAction(e -> root.setCenter(bookingSection));
        reportsVisualization.setOnAction(e -> showVehicleReport(stage));
        dashboard.setOnAction(e -> createDashboard()); // Refresh the dashboard
        logoutButton.setOnAction(e -> stage.close());

        sidebar.getChildren().addAll(logo, dashboard, vehicleManagement, customerManagement, bookingSystem, paymentBilling, reportsVisualization, logoutButton);

        root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(dashboardSection);  // Default

        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Vehicle Rental System - " + loggedInUser.role);
        stage.show();
    }

    private void createDashboard() {
        Label title = new Label("📊 Dashboard Overview");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label todayStats = new Label("Today’s Statistics");
        todayStats.setStyle("-fx-font-size: 16px; -fx-underline: true;");
        Label totalVehicles = new Label();
        Label totalCustomers = new Label();
        Label availableCount = new Label();
        Label bookedCount = new Label();

        Label earningSummary = new Label("Earning Summary: ₹" + (vehicles.size() * 5000));
        earningSummary.setStyle("-fx-font-size: 14px; -fx-text-fill: green;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search Vehicle by ID");
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> {
            String search = searchField.getText().trim().toLowerCase();
            for (Vehicle v : vehicles) {
                if (v.id.toLowerCase().equals(search)) {
                    showAlert("Vehicle Found", v.toString());
                    return;
                }
            }
            showAlert("Not Found", "No vehicle matches that ID.");
        });

        HBox searchBox = new HBox(10, searchField, searchBtn);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        Runnable updateStats = () -> {
            long available = vehicles.stream().filter(v -> v.status.equalsIgnoreCase("Available")).count();
            long booked = vehicles.stream().filter(v -> v.status.equalsIgnoreCase("Booked")).count();
            totalVehicles.setText("Total Vehicles: " + vehicles.size());
            totalCustomers.setText("Total Customers: " + customers.size());
            availableCount.setText("Available Vehicles: " + available);
            bookedCount.setText("Booked Vehicles: " + booked);
        };
        updateStats.run();

        VBox statsBox = new VBox(5, todayStats, totalVehicles, totalCustomers, availableCount, bookedCount, earningSummary);
        statsBox.setPadding(new Insets(10));
        statsBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 8; -fx-background-radius: 8;");

        VBox mainBox = new VBox(15, title, statsBox, new Label("🔍 Quick Search:"), searchBox);
        mainBox.setPadding(new Insets(15));
        mainBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10,0,0,4);");

        dashboardSection = mainBox;
        root.setCenter(dashboardSection);
    }

    private void createVehicleSection() {
        ListView<Vehicle> vehicleListView = new ListView<>(vehicles);
        vehicleListView.setPrefHeight(150);
        TextField vehicleId = new TextField(); vehicleId.setPromptText("ID");
        TextField brand = new TextField(); brand.setPromptText("Brand");
        TextField model = new TextField(); model.setPromptText("Model");

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Car", "Bike", "Van", "Truck");
        categoryBox.setPromptText("Category");

        TextField rate = new TextField(); rate.setPromptText("Rate/Day");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Available", "Booked");
        statusBox.setPromptText("Status");

        TextField imagePathField = new TextField(); imagePathField.setPromptText("Image Path");
        ImageView vehicleImageView = new ImageView(); vehicleImageView.setFitHeight(100); vehicleImageView.setFitWidth(100);

        Button addVehicle = new Button("Add Vehicle");
        Button deleteVehicle = new Button("Delete Vehicle");
        Button updateVehicle = new Button("Update Vehicle");
        TextField searchField = new TextField(); searchField.setPromptText("Search by ID");
        Button searchButton = new Button("Search");

        addVehicle.setOnAction(e -> {
            try {
                vehicles.add(new Vehicle(
                        vehicleId.getText(), brand.getText(), model.getText(),
                        categoryBox.getValue(), Double.parseDouble(rate.getText()),
                        statusBox.getValue(), imagePathField.getText()
                ));
                clearFields(vehicleId, brand, model, rate, imagePathField);
                categoryBox.setValue(null);
                statusBox.setValue(null);
            } catch (Exception ex) {
                showAlert("Error", "Please check input fields!");
            }
        });

        deleteVehicle.setOnAction(e -> {
            Vehicle selected = vehicleListView.getSelectionModel().getSelectedItem();
            if (selected != null) vehicles.remove(selected);
        });

        updateVehicle.setOnAction(e -> {
            Vehicle selected = vehicleListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.brand = brand.getText();
                selected.model = model.getText();
                selected.category = categoryBox.getValue();
                selected.ratePerDay = Double.parseDouble(rate.getText());
                selected.status = statusBox.getValue();
                selected.imagePath = imagePathField.getText();
                vehicleListView.refresh();
                vehicleImageView.setImage(new Image("file:" + selected.imagePath));
            }
        });

        searchButton.setOnAction(e -> {
            String search = searchField.getText().trim().toLowerCase();
            for (Vehicle v : vehicles) {
                if (v.id.toLowerCase().equals(search)) {
                    showAlert("Vehicle Found", v.toString());
                    return;
                }
            }
            showAlert("Not Found", "No vehicle matches that ID.");
        });

        vehicleListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                vehicleId.setText(newV.id);
                brand.setText(newV.brand);
                model.setText(newV.model);
                categoryBox.setValue(newV.category);
                rate.setText(String.valueOf(newV.ratePerDay));
                statusBox.setValue(newV.status);
                imagePathField.setText(newV.imagePath);
                vehicleImageView.setImage(new Image("file:" + newV.imagePath));
            }
        });

        vehicleSection = new VBox(8, vehicleId, brand, model, categoryBox, rate, statusBox, imagePathField,
                new HBox(5, addVehicle, updateVehicle, deleteVehicle), new HBox(5, searchField, searchButton),
                vehicleListView, vehicleImageView);
        vehicleSection.setPadding(new Insets(10));
        vehicleSection.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10,0,0,4);");
    }

    private void createCustomerSection() {
        ListView<Customer> customerListView = new ListView<>(customers);
        customerListView.setPrefHeight(150);
        TextField customerName = new TextField(); customerName.setPromptText("Name");
        TextField customerContact = new TextField(); customerContact.setPromptText("Contact");
        TextField customerLicense = new TextField(); customerLicense.setPromptText("License No.");
        Button addCustomer = new Button("Add Customer");

        addCustomer.setOnAction(e -> {
            if (!customerName.getText().isEmpty()) {
                customers.add(new Customer(customerName.getText(), customerContact.getText(), customerLicense.getText()));
                clearFields(customerName, customerContact, customerLicense);
            } else {
                showAlert("Error", "Customer name cannot be empty.");
            }
        });

        customerSection = new VBox(8, customerName, customerContact, customerLicense, addCustomer, customerListView);
        customerSection.setPadding(new Insets(10));
        customerSection.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10,0,0,4);");
    }

    private void createBookingSection() {
        DatePicker startDate = new DatePicker();
        DatePicker endDate = new DatePicker();
        ComboBox<String> paymentMethod = new ComboBox<>(FXCollections.observableArrayList("Cash", "Credit Card", "Online"));
        paymentMethod.setPromptText("Select Payment Method");
        Button bookVehicle = new Button("Book Vehicle");
        Button generateBill = new Button("Generate Bill");
        Button showReport = new Button("Show Available Vehicle Report");

        bookVehicle.setOnAction(e -> showAlert("Booking Section", "Book Vehicle feature triggered."));
        generateBill.setOnAction(e -> showAlert("Billing", "Billing feature triggered."));
        showReport.setOnAction(e -> showVehicleReport(null));

        bookingSection = new VBox(8, startDate, endDate, paymentMethod, bookVehicle, generateBill, showReport);
        bookingSection.setPadding(new Insets(10));
        bookingSection.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10,0,0,4);");
    }

    private void showVehicleReport(Stage stage) {
        PieChart chart = new PieChart();
        long available = vehicles.stream().filter(v -> v.status.equals("Available")).count();
        long booked = vehicles.size() - available;
        chart.getData().add(new PieChart.Data("Available", available));
        chart.getData().add(new PieChart.Data("Booked", booked));
        Stage chartStage = new Stage();
        chartStage.setScene(new Scene(new VBox(chart), 400, 300));
        chartStage.setTitle("Vehicle Availability Report");
        chartStage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields(TextField... fields) {
        for (TextField f : fields) f.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
