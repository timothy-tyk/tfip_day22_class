package workshop21.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import workshop21.app.model.Employee;
import workshop21.app.repository.EmployeeRepo;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
  @Autowired
  EmployeeRepo employeeRepo;

  @GetMapping("/")
  public List<Employee> retrieveEmployees(){
    List<Employee> employees= employeeRepo.getEmployeeList();
    if(employees.isEmpty()){
      return null;
    }
    else{
      return employees;
    }
  }
}
