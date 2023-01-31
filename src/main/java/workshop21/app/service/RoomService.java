package workshop21.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import workshop21.app.model.Room;
import workshop21.app.repository.RoomRepo;

@Service
public class RoomService{

  @Autowired
  RoomRepo roomRepo;

  public int count() {
    return roomRepo.count();
  }

  public Boolean save(Room room) {
    return roomRepo.save(room);
  }

  public List<Room> findAll() {
    return roomRepo.findAll();
  }

  public Room findById(Integer id) {
    return roomRepo.findById(id);
  }

  public int update(Room room) {
    return roomRepo.update(room);
  }

  public Integer deleteById(Integer id) {
    return roomRepo.deleteById(id);
  }
}
