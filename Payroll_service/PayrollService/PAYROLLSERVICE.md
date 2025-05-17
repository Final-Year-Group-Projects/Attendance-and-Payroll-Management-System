# Payroll Service

The **Payroll Service** is a Spring Boot microservice responsible for managing employee payrolls, generating payslips (HTML and PDF), handling reimbursements, and providing payroll notifications.

---

##  Features

- Create, read, update, delete payroll records 
- Generate payslip in HTML and PDF formats
- Notify employee with latest payroll info
- Submit and manage reimbursement requests
- Integration with User and Attendance services via Feign clients
- Centralized exception handling and validation
- Role-based endpoint segregation (Admin vs User)

---

##  API Endpoints

###  Admin Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/payrolls` | Create new payroll |
| GET    | `/payrolls/{payrollId}` | Get payroll by ID |
| PUT    | `/payrolls/{payrollId}` | Update payroll by ID |
| DELETE | `/payrolls/{payrollId}` | Delete payroll by ID |
| DELETE | `/payrolls/employee/{employeeId}` | Delete all payrolls for an employee |
| GET    | `/payrolls/all` | Get all payroll records |
| POST   | `/payrolls/generateAll?month={month}&year={year}` | Bulk payroll generation |
| PUT    | `/payrolls/{payrollId}/status?status={status}` | Update payroll status |
| PUT    | `/reimbursements/{id}/status?status={status}` | Approve or reject reimbursement |
| DELETE | `/reimbursements/{id}` | Delete reimbursement request |

###  User Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/payrolls/employee/{employeeId}` | View own payroll records |
| GET    | `/payrolls/{payrollId}/payslip` | View payslip (HTML) |
| GET    | `/payrolls/{payrollId}/payslip/pdf` | Download payslip as PDF |
| POST   | `/payrolls/{employeeId}/notify` | Request payroll notification |
| POST   | `/reimbursements` | Submit reimbursement |
| GET    | `/reimbursements/employee/{employeeId}` | View own reimbursements |

---

##  Integrations

- **User Service**: fetch user info and roles
- **Attendance Service**: retrieve attendance data for payroll calculation

---

