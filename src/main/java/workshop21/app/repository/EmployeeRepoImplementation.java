package workshop21.app.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
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

  String getEmployeeByIdSQL = """
    select e.id emp_id, e.first_name, e.last_name, e.salary,
    d.id dpt_id,d.dependent_name,d.relationship,d.birth_date 
    from employee e 
    inner join dependent d
    on e.id = d.employee_id
    WHERE e.id =?;
    """;

    String addEmployeeSQL = "INSERT INTO employee (first_name, last_name, salary) VALUES(?,?,?)";

    String updateEmployeeSQL = "UPDATE employee SET first_name = ?, last_name=?,salary=? WHERE id = ?";

    String deleteEmployeeSQL = "DELETE FROM employee WHERE id=?";

  @Override
  public List<Employee> getEmployeeDependents() {
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
            emp.getDependents().add(dep);
            employees.add(emp);
          }else{
            List<Employee> employeeList = employees.stream().filter(e->e.getId()==emp.getId()).collect(Collectors.toList());
            // if employee is not found in list, add to employees
            if(employeeList.size()==0){
              emp.getDependents().add(dep);
              System.out.println(dep);
              employees.add(emp);
            }else{
              int index = employees.indexOf(employeeList.get(0));
              if(index>=0){
                employees.get(index).getDependents().add(dep);
                System.out.println(dep);
                }
              }
            }
          }
        
        return employees;
      }
      
    });
   
  }
  @Override
  public Boolean addEmployee(Employee emp) {
    System.out.println(emp);
    Boolean added = true;
    jdbcTemplate.execute(addEmployeeSQL, new PreparedStatementCallback<Boolean>() {

      @Override
      public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
        ps.setString(1, emp.getFirstName());
        ps.setString(2, emp.getLastName());
        ps.setInt(3, emp.getSalary());
        Boolean result = ps.executeUpdate()>0;
        return result;
      }
      
    });
    return added;
  }
  @Override
  public Employee getEmployeeById(Integer id) {
  //  Employee emp =  jdbcTemplate.queryForObject(getEmployeeByIdSQL, BeanPropertyRowMapper.newInstance(Employee.class), id);
    // return emp;
    return jdbcTemplate.query(getEmployeeByIdSQL, new ResultSetExtractor<Employee>() {

      @Override
      public Employee extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Employee> employees = new ArrayList<Employee>();
        while(rs.next()){
          Employee emp = new Employee();
          emp.setId(rs.getInt("emp_id"));
          emp.setFirstName(rs.getString("first_name"));
          emp.setLastName(rs.getString("last_name"));
          emp.setDependents(new ArrayList<Dependent>());
          emp.setSalary(rs.getInt("salary"));

          Dependent dep = new Dependent();
          dep.setId(rs.getInt("dpt_id"));
          dep.setDependentName(rs.getString("dependent_name"));
          dep.setRelationship(rs.getString("relationship"));
          dep.setBirthDate(rs.getDate("birth_date"));

          emp.getDependents().add(dep);
          if(employees.size()==0){
            // Add employee to holding list
            employees.add(emp);
          } else{
            // employee has >1 dependent
            employees.get(employees.size()-1).getDependents().add(dep);
            // add current dependent to employee obj in list
          }
        }
        return employees.get(0);
      }
      
    },id);
  }
  @Override
  public Integer updateEmployee(Employee emp) {
    Integer updated=0;
    PreparedStatementSetter pss = new PreparedStatementSetter() {

      @Override
      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, emp.getFirstName());
        ps.setString(2, emp.getLastName());
        ps.setInt(3, emp.getSalary());
        ps.setInt(4, emp.getId());
        
      }
    };
    updated = jdbcTemplate.update(updateEmployeeSQL, pss);
    return updated;
  }

  @Override
  public Integer deleteEmployeeById(Integer id) {
    Integer deleted = 0;
    PreparedStatementSetter pss = new PreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setInt(1, id);
      }
      
    };
    deleted = jdbcTemplate.update(deleteEmployeeSQL, pss);
    return deleted;
  }

  @Override
  public List<Employee> getEmployeeList() {
    List<Employee> employees = jdbcTemplate.query(getAllEmployeeSQL,BeanPropertyRowMapper.newInstance(Employee.class));
    return employees;
  }
  

  
  
  
}


            