package com.ga.concurrency.controller;

import com.ga.concurrency.model.Employee;
import com.ga.concurrency.service.EmployeeCategoryService;
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
    private EmployeeCategoryService employeeCategoryService;

    @GetMapping("/employees/exceptional")
    public List<String> getExceptionalEmployees() throws IOException, InterruptedException {
        return employeeCategoryService.getEmployees("exceptional");
    }

    @GetMapping("/employees/good")
    public List<String> getGoodEmployees() throws IOException, InterruptedException {
        return employeeCategoryService.getEmployees("good");
    }

    @GetMapping("/employees/average")
    public List<String> getAverageEmployees() throws IOException, InterruptedException {
        return employeeCategoryService.getEmployees("average");
    }

    @GetMapping("/employees/bad")
    public List<String> getBadEmployees() throws IOException, InterruptedException {
        return employeeCategoryService.getEmployees("bad");
    }

    @PostMapping("/threadsCount")
    public String setThreadsCount(@RequestParam("count") int count) {
        if (count < 1)
            return "Thread count must be at least 1.";
        employeeFileReader.setThreadsCount(count);
        return "Thread count updated to " + count;
    }

    @PostMapping("/employees/process")
    public String process() throws IOException, InterruptedException {
        String fileName = "employeesData/test_employees.csv";

        List<Employee> employees = employeeFileReader.read(fileName);

        return employeeFileWriter.write(fileName, employees);
    }
}
