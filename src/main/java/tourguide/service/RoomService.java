package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tourguide.exception.BadRequestException;
import tourguide.exception.NotFoundException;
import tourguide.model.Room;
import tourguide.model.Tour;
import tourguide.model.User;
import tourguide.payload.MemberDTO;
import tourguide.repository.RoomRepository;

import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserService userService;

//    @Autowired
//    TourService tourService;

    public Room findById(Long id){
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if(optionalRoom.isEmpty()){
            throw new NotFoundException("Không tìm thấy phòng");
        }
        return optionalRoom.get();
    }

    public Room createRoom(Long userId, Tour tour){
        User user = userService.findById(userId);
        Room room = new Room();
        room.setRoomTour(tour);
        room.setRoomUser(user);
        room.setIsApproved(true);
        return roomRepository.save(room);
    }

    public Room joinRoom(Long userId, Tour tour){
        User user = userService.findById(userId);
        Room room = new Room();
        room.setRoomTour(tour);
        room.setRoomUser(user);
        room.setIsApproved(false);
        return roomRepository.save(room);
    }

//    public Room approveMember(Long userId, Tour tour){
//        User user = userService.findById(userId);
//        Room room = findByUserIdAndTourId(userId, tour.getId());
//        room.setIsApproved(true);
//        return roomRepository.save(room);
//    }

//    @Transactional
    public void deleteMember(Long id){
        Room room = findById(id);
         roomRepository.delete(room);
    }
}