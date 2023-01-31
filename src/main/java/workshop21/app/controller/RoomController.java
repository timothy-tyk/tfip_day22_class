package workshop21.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import workshop21.app.model.Room;
import workshop21.app.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
  @Autowired
  RoomService roomService;

  // @GetMapping("/count")
  // public Integer getRoomCount(){
  //   Integer roomCount = roomService.count();
  //   return roomCount;
  // }
  @GetMapping("/count")
  public ResponseEntity<String> getRoomCount(){
    Integer roomCount = roomService.count();
    JsonObject response = Json.createObjectBuilder()
                          .add("roomCount", roomCount)
                          .build();
    return ResponseEntity.status(HttpStatus.OK).body(response.toString());
  }
  @GetMapping("/")
  public ResponseEntity<List<Room>> retreiveAllRooms(){
    List<Room> rooms = new ArrayList<Room>();
    rooms = roomService.findAll();
    if(rooms.isEmpty()){
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return ResponseEntity.status(HttpStatus.OK).body(rooms);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Room> retreiveRoomById(@PathVariable Integer id){
    Room room = roomService.findById(id);
    if (room == null){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.status(HttpStatus.OK).body(room);
  }

  @PostMapping("/")
  public ResponseEntity<Room> createRoom(@RequestBody Room room){
    Boolean result = roomService.save(room);
    if(!result){
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(room);
  }

  @PutMapping("/")
  public ResponseEntity<Integer> updateRoom(@RequestBody Room room){
    int updated = roomService.update(room);
    if(updated == 1){
      return new ResponseEntity<>(1,HttpStatus.OK);
    }
    else{
      return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Integer> deleteRoomById(@PathVariable Integer id){
    Integer result = roomService.deleteById(id);
    if(result == 0){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }else{
      return new ResponseEntity<Integer>(1,HttpStatus.OK);
    }
  }
}
