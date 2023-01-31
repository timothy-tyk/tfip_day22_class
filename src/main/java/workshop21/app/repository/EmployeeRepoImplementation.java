package workshop21.app.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import workshop21.app.model.Dependent;
import workshop21.app.model.Employee;
@Repository
public class EmployeeRepoImplementation implements EmployeeRepo{

  @Autowired
  JdbcTemplate jdbcTemplate;

  String getAllEmployeeSQL = "SELECT * FROM employee";

  String getALlEmployeeAndDependentsSQL = 
  """
    select e.id emp_id, e.first_name, e.last_name, e.salary,
    d.id dpt_id,d.dependent_name,d.relationship,d.birth_date 
    from employee e 
    inner join dependent d
    on e.id = d.employee_id;
    """;
  @Override
  public List<Employee> getEmployeeList() {
    return jdbcTemplate.query(getALlEmployeeAndDependentsSQL, new ResultSetExtractor<List<Employee>>() {
      // need ResultSetExtractor because we are mapping data from 2 tables (employee and dependent)
      @Override
      public List<Employee> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Employee> employees = new ArrayList<Employee>();
        while(rs.next()){
          Employee emp = new Employee();
         
          Integer empId = rs.getInt("emp_id");
          emp.setId(empId);
          emp.setFirstName(rs.getString("first_name"));
          emp.setLastName(rs.getString("last_name"));
          emp.setSalary(rs.getInt("salary"));
          emp.setDependents(new ArrayList<Dependent>());

          Dependent dep = new Dependent();
          dep.setId(rs.getInt("dpt_id"));
          dep.setDependentName(rs.getString("dependent_name"));
          dep.setRelationship(rs.getString("relationship"));
          dep.setBirthDate(rs.getDate("birth_date"));
          if(employees.size()==0){
            employees.add(emp);
          }else{
            List<Employee> employeeList = employees.stream().filter(e->e.getId()==emp.getId()).collect(Collectors.toList());
            // if employee is not found in list, add to employees
            if(employeeList.size()==0){
              emp.getDependents().add(dep);
              employees.add(emp);
            }else{
              int index = employees.indexOf(employeeList.get(0));
              if(index>=0){
                employees.get(index).getDependents().add(dep);
              }
            }
          }
        }
        return employees;
      }
      
    });
   
  }
  
  
}
