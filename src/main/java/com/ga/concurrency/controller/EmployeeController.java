package com.ga.concurrency.controller;

import com.ga.concurrency.model.Employee;
import com.ga.concurrency.service.EmployeeFileReader;
import com.ga.concurrency.service.EmployeeFileWriter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class EmployeeController {

    private EmployeeFileReader employeeFileReader;
    private EmployeeFileWriter employeeFileWriter;

    @PostMapping("/employees/process")
    public String process() throws IOException, InterruptedException {
        String fileName = "employeesData/test_employees.csv";

        List<Employee> employees = employeeFileReader.read(fileName);

        return employeeFileWriter.write(fileName, employees);
    }
}
