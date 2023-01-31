package workshop21.app;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import workshop21.app.model.Employee;
import workshop21.app.repository.EmployeeRepo;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmployeeRepoTest {
  @Autowired
  EmployeeRepo employeeRepo;

  @Test
  public void retrieveAllEmployees(){
    List<Employee> empList = employeeRepo.getEmployeeList();
    empList.forEach(emp -> System.out.println(emp));
    assertTrue(empList.size()>0);
  }
}
