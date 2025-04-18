package com.example.demo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {
    public static void exportVehiclesToCSV(List<HelloApplication.Vehicle> vehicles, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ID,Brand,Model,Category,Rate,Status,ImagePath\n");
            for (HelloApplication.Vehicle v : vehicles) {
                writer.write(String.format("%s,%s,%s,%s,%.2f,%s,%s\n",
                        v.id, v.brand, v.model, v.category, v.ratePerDay, v.status, v.imagePath));
            }
            System.out.println("Exported to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

