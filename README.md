# Concurrent CSV Data Processor

A Spring Boot application that processes employee CSV data using a configurable thread pool. It calculates salary changes based on years worked, role, and project completion, then saves the results in a new file.

The project focuses on practising file I/O, thread pooling, and concurrency control without using a database.

## Setup

Requires Java 17. Maven Wrapper is included.

Run from the project root:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs on port `8080`. The input file must be at:

```text
employeesData/test_employees.csv
```

## Employee Data and Salary Rules

The input has no header and follows this order:

```text
id,name,salary,dateJoined,role,projectCompletionPercentage
```

Example:

```csv
1,Alice,52000.0,2019-05-12,Employee,0.8
```

Completion is stored as a fraction, so `0.8` means 80%.

Eligible employees receive 2% for each completed year, plus their role increase: Director 5%, Manager 2%, and Employee 1%. Years worked are calculated using the current date.

| Category | Project completion | Salary change |
|---|---|---|
| Bad | Below 40% | 1% deduction |
| Average | 40% to below 60% | No change |
| Good | 60% to 80%, inclusive | Normal raise |
| Exceptional | Above 80% | Normal raise percentage multiplied by 1.5 |

For example, a Manager with three completed years normally receives 8%. If completion is above 80%, the increase becomes 12%.

Results are rounded to two decimal places and sorted by employee ID. The output includes an extra column showing the change, such as `+12.0%`, `-1.0%`, or `0.0%`.

Generated files are saved in `employeesData`:

```text
test_employeesUpdated2026-10-20_14-30-45-123.csv
```

The original file is kept unchanged.

## Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/employees/process` | Process employees and return the generated file path |
| POST | `/api/threadsCount?count=10` | Change the thread count for future processing |
| GET | `/api/employees/exceptional` | Print and return exceptional employees |
| GET | `/api/employees/good` | Print and return good employees |
| GET | `/api/employees/average` | Print and return average employees |
| GET | `/api/employees/bad` | Print and return bad employees |

The default thread count is five. It must be positive and resets when the application restarts.

Category endpoints return complete CSV lines from the latest updated file. If none exists, they generate one first. To refresh existing results after changing the input, call the processing endpoint again.

## Code Structure

| Class | Responsibility |
|---|---|
| `Employee` and `Role` | Employee data and supported roles |
| `EmployeeController` | HTTP endpoints |
| `EmployeeFileReader` | Read lines and submit employee-processing tasks |
| `SalaryCalculationService` | Calculate salary and record the change percentage |
| `EmployeeFileWriter` | Sort employees and write the updated CSV |
| `EmployeeCategoryService` | Find the latest output and filter employees |

## Bonus Features

- **High-performance bonus:** Employees with project completion above 80% receive 1.5 times their normal raise percentage.
- **Salary deduction:** Employees with project completion below 40% receive a 1% salary deduction.
- **Configurable thread count:** An endpoint allows changing the processing pool size, with five threads as the default.
- **Updated CSV output:** Results are saved in a separate timestamped file, keeping the original unchanged.
- **Sorted results:** Employees are sorted by ID using `Arrays.parallelSort` before writing.
- **Salary-change column:** Each output row includes the applied increase or deduction, such as `+12.0%`, `-1.0%`, or `0.0%`.
- **Employee categories:** Four endpoints print and return exceptional, good, average, and bad employees based on project completion.
