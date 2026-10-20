package com.ga.concurrency.service;

import com.ga.concurrency.model.Employee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class SalaryCalculationService {

    public double calculateSalary(Employee employee) {
        // opposite of the bonus. deduct salary if proj comp rate is less than 40%
        if (employee.getProjCompletionPercentage() < 0.4) {
            double newSalary = employee.getSalary() * 0.99;
            return Math.round(newSalary * 100.0) / 100.0;
        }

        if (employee.getProjCompletionPercentage() < 0.6)
            return employee.getSalary();

        int yearsWorked = (int) ChronoUnit.YEARS.between(
                employee.getDateJoined(), LocalDate.now());

        double increasePercentage = 0;

        if (yearsWorked > 0)
            increasePercentage = yearsWorked * 0.02;

        switch (employee.getRole()) {
            case DIRECTOR:
                increasePercentage += 0.05;
                break;
            case MANAGER:
                increasePercentage += 0.02;
                break;
            case EMPLOYEE:
                increasePercentage += 0.01;
                break;
        }

        // bonus
        if (employee.getProjCompletionPercentage() > 0.8)
            increasePercentage *= 1.5;

        double newSalary = employee.getSalary() * (1 + increasePercentage);

        return Math.round(newSalary * 100.0) / 100.0;
    }
}
