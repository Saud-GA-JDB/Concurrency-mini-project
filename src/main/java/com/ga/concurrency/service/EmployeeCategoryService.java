package com.ga.concurrency.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class EmployeeCategoryService {

    private EmployeeFileReader employeeFileReader;
    private EmployeeFileWriter employeeFileWriter;

    public List<String> getEmployees(String category) throws IOException, InterruptedException {
        String fileName = getLatestUpdatedFile();
        List<String> employees = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ( (line = reader.readLine()) != null) {
                String[] values = line.split(",", -1);
                double completion = Double.parseDouble(values[5].trim());
                boolean matches = false;

                switch (category) {
                    case "exceptional":
                        matches = completion > 0.8;
                        break;
                    case "good":
                        matches = completion >= 0.6 && completion <= 0.8;
                        break;
                    case "average":
                        matches = completion >= 0.4 && completion < 0.6;
                        break;
                    case "bad":
                        matches = completion < 0.4;
                        break;
                }

                if (matches) {
                    System.out.println(line);
                    employees.add(line);
                }
            }
        }

        return employees;
    }

    private String getLatestUpdatedFile() throws IOException, InterruptedException {
        List<Path> updatedFiles;

        try (Stream<Path> paths = Files.list(Paths.get("employeesData"))) {
            updatedFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString()
                            .startsWith("test_employeesUpdated"))
                    .filter(path -> path.getFileName().toString().endsWith(".csv"))
                    .collect(Collectors.toList());
        }

        if (updatedFiles.isEmpty()) {
            String fileName = "employeesData/test_employees.csv";
            return employeeFileWriter.write(fileName, employeeFileReader.read(fileName));
        }

        Path latestFile = updatedFiles.get(0);

        for (Path file : updatedFiles) {
            if (file.getFileName().toString().compareTo(
                    latestFile.getFileName().toString()) > 0) {
                latestFile = file;
            }
        }

        return latestFile.toString();
    }
}
