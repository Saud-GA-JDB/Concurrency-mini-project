package com.ga.concurrency.service;

import com.ga.concurrency.model.Employee;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmployeeFileWriter {

    public String write(String fileName, List<Employee> employees) throws IOException {
        File inputFile = new File(fileName);

        String name = inputFile.getName();
        String baseName = name.substring(0, name.lastIndexOf("."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");
        String timestamp = LocalDateTime.now().format(formatter);
        File outputFile = new File(inputFile.getParent(), baseName + "Updated" + timestamp + ".csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (Employee employee : employees) {
                writer.write(
                        employee.getId() + "," +
                        employee.getName() + "," +
                        employee.getSalary() + "," +
                        employee.getDateJoined() + "," +
                        employee.getRole() + "," +
                        employee.getProjCompletionPercentage());
                writer.newLine();
            }
        }
        return outputFile.getPath();
    }
}
