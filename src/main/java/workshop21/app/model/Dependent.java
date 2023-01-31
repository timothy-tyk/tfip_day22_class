package workshop21.app.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dependent {
  private Integer id;
  private String dependentName;
  private String relationship;
  private Date birthDate;
  private Employee employee;

}
