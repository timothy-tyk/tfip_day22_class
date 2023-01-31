package workshop21.app.repository;

import java.util.List;

import workshop21.app.model.Room;

public interface RoomRepo {
  int count();

  // Create
  Boolean save(Room room);

  // Read All
  List<Room> findAll();
  
  // Read 1 record
  Room findById(Integer id);

  // Update
  int update(Room room);

  // Delete
  Integer deleteById(Integer id);
}
