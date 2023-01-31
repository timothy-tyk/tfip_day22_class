package workshop21.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

  @GetMapping("/dependents")
  public List<Employee> retrieveEmployeeAndDependents(){
    List<Employee> employees= employeeRepo.getEmployeeDependents();
    if(employees.isEmpty()){
      return null;
    }
    else{
      return employees;
    }
  }

  @PostMapping("/")
  public ResponseEntity<Employee> createEmployee(@RequestBody Employee emp){
    Boolean added = employeeRepo.addEmployee(emp);
    if(!added){
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<Employee>(emp,HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Integer id){
    Employee emp = employeeRepo.getEmployeeById(id);
    if (emp == null){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<Employee>(emp,HttpStatus.FOUND);
  }

  @PutMapping("/")
  public ResponseEntity<Integer> updateEmployeeById(@RequestBody Employee emp){
    Integer result = employeeRepo.updateEmployee(emp);
    if(result==0){
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Integer>(result, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Integer> deleteEmployeeById(@PathVariable("id") Integer id){
    Integer deleted = employeeRepo.deleteEmployeeById(id);
    if(deleted==0){
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
;    }
    return new ResponseEntity<Integer>(deleted, HttpStatus.OK);
  }
}
