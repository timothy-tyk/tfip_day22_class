package workshop21.app.repository;

import java.util.List;

import workshop21.app.model.Employee;

public interface EmployeeRepo {
  
  List<Employee> getEmployeeList();
  List<Employee> getEmployeeDependents();
// Create
  Boolean addEmployee(Employee emp);
// Read
  Employee getEmployeeById(Integer id);
// Update
  Integer updateEmployee(Employee emp);
// Delete
  Integer deleteEmployeeById(Integer id);

}
