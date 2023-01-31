package workshop21.app.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
  private Integer id;
  private String firstName;
  private String lastName;
  private Integer salary;
  
  // One to many relationship
  private List<Dependent> dependents;
}
