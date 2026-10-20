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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class EmployeeFileReader {

    private SalaryCalculationService salaryCalculationService;
    private int threadsCount = 5;
    private final ReentrantLock threadsCountLock = new ReentrantLock();

    public EmployeeFileReader(SalaryCalculationService salaryCalculationService) {
        this.salaryCalculationService = salaryCalculationService;
    }

    public void setThreadsCount(int threadsCount) {
        threadsCountLock.lock();
        try {
            this.threadsCount = threadsCount;
        } finally {
            threadsCountLock.unlock();
        }
    }

    public int getThreadsCount() {
        threadsCountLock.lock();
        try {
            return threadsCount;
        } finally {
            threadsCountLock.unlock();
        }
    }

    public List<Employee> read(String filename) throws IOException, InterruptedException {
        List<Employee> employees = new ArrayList<>();

        ReentrantLock lock = new ReentrantLock();
        ExecutorService executor = Executors.newFixedThreadPool(getThreadsCount());

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ( (line = reader.readLine()) != null) {
                final String employeeLine = line;

                executor.submit( () -> {
                    String[] values = employeeLine.split(",", -1);

                    Employee employee = new Employee();

                    employee.setId(Long.parseLong(values[0].trim()));
                    employee.setName(values[1].trim());
                    employee.setSalary(Double.parseDouble(values[2].trim()));
                    employee.setDateJoined(LocalDate.parse(values[3].trim()));
                    employee.setRole(Role.valueOf(values[4].trim().toUpperCase()));
                    employee.setProjCompletionPercentage(
                            Double.parseDouble(values[5].trim()));

                    employee.setSalary(salaryCalculationService.calculateSalary(employee));

                    lock.lock();
                    try {
                        employees.add(employee);
                    } finally {
                        lock.unlock();
                    }
                });
            }
        } finally {
            executor.shutdown();
        }

        while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {}

        return employees;
    }
}
