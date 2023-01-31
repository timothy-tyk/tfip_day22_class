package workshop21.app.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import workshop21.app.model.Room;

@Repository
public class RoomRepoImplementation implements RoomRepo{
  @Autowired
  JdbcTemplate jdbcTemplate;

  String countSQL = "SELECT count(*) FROM room";
  String selectSQL = "SELECT * FROM room";
  String selectByIdSQL = "SELECT * FROM room WHERE id = ?";
  String insertSQL = "INSERT INTO room (room_type, price) VALUES (?, ?)";
  String updateSQL = "UPDATE room SET room_type = ?, price = ? WHERE id = ?";
  String deleteSQL = "DELETE FROM room WHERE id = ?";

  @Override
  public int count() {
    Integer result = 0;
    result = jdbcTemplate.queryForObject(countSQL, Integer.class);
    if(result == null){
      return 0;
    }
    return result;
  }

  @Override
  public Boolean save(Room room) {
    Boolean saved = true;
    // jdbcTemplate.execute(countSQL);
    saved = jdbcTemplate.execute(insertSQL, new PreparedStatementCallback<Boolean>() {
      // Callback function - async function

      @Override
      public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
        ps.setString(1, room.getRoomType());
        ps.setInt(2, room.getPrice());
        Boolean result = ps.executeUpdate()>0; 
        //executeupdate returns 0 if not inserted, returns 1 if inserted
        System.out.println(result);
        return result;
      }
      
    });
    return saved;
  }

  @Override
  public List<Room> findAll() {
    List<Room> result = new ArrayList<Room>();
    result =  jdbcTemplate.query(selectSQL, BeanPropertyRowMapper.newInstance(Room.class));
    // BeanPropertyRowMapper : automatic mapping of parameters as long as they match (according to Room class in this example)
    return result;
  }

  @Override
  public Room findById(Integer id) {
    Room result = jdbcTemplate.queryForObject(selectByIdSQL ,BeanPropertyRowMapper.newInstance(Room.class) , id);
    return result;
  }

  @Override
  public int update(Room room) {
    int updated=0;
    updated = jdbcTemplate.update(updateSQL, new PreparedStatementSetter() {

      @Override
      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, room.getRoomType());
        ps.setInt(2, room.getPrice());
        ps.setInt(3, room.getId());
      }
      
    });
    return updated;
  }

  @Override
  public Integer deleteById(Integer id) {
    Integer deleted = 0;
    PreparedStatementSetter pss = new PreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setInt(1, id);
      }
    };
    deleted = jdbcTemplate.update(deleteSQL, pss);
    return deleted;
  }
  
}
