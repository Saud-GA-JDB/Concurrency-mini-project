package com.ga.concurrency.service;

import com.ga.concurrency.model.Employee;
import com.ga.concurrency.model.Role;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeFileReader {

    public List<Employee> read(String filename) throws IOException {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ( (line = reader.readLine()) != null) {
                String[] values = line.split(",", -1);

                Employee employee = new Employee();

                employee.setId(Long.parseLong(values[0].trim()));
                employee.setName(values[1].trim());
                employee.setSalary(Double.parseDouble(values[2].trim()));
                employee.setDateJoined(LocalDate.parse(values[3].trim()));
                employee.setRole(Role.valueOf(values[4].trim().toUpperCase()));
                employee.setProjCompletionPercentage(Double.parseDouble(values[5].trim()));

                employees.add(employee);
            }
        }

        return employees;
    }
}
