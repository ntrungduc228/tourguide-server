package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tourguide.exception.BadRequestException;
import tourguide.exception.NotFoundException;
import tourguide.model.*;
import tourguide.payload.*;
import tourguide.repository.DestinationRepository;
import tourguide.repository.RoomRepository;
import tourguide.repository.TourRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TourService {

    @Autowired
    TourRepository tourRepository;

    @Autowired
    DestinationRepository destinationRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoomService roomService;

    @Autowired
    NotificationService notificationService;



    public List<Tour> getListTourByUserId(Long userId){
        List<Tour> tours = new ArrayList<>();
        List<Room> rooms = roomService.getListRoomByUserId(userId);
//        System.out.println("ffff" + rooms.size());
        if(rooms.size() == 0){
            return tours;
        }

        for (Room room : rooms){
            tours.add(room.getRoomTour());
        }
        return tours;
    }

//    public Tour getCurTour(Long userId){
//
//    }

    public List<NotificationDTO> notificationTourMember(Tour tour, NotificationType type, Long creatorId){
        System.out.println("tour size " + tour.getId() + " " + tour.getRooms().size());
        List<NotificationDTO> notifications = new ArrayList<>();
        if(tour.getRooms() == null || tour.getRooms().size() == 0){
            return new ArrayList<>();
        }

        for(Room room : tour.getRooms()){
//            if(room.getRoomUser().getId() != creatorId){
                NotificationDTO notificationDTO = new NotificationDTO().builder()
                        .isRead(false)
                        .receiverId(room.getRoomUser().getId())
                        .creatorId(creatorId)
                        .content(String.valueOf(type))
                        .build();
                NotificationDTO notification = notificationService.createNotification(notificationDTO);
//                simpMessagingTemplate.convertAndSend("/topic/noti/" + room.getRoomUser().getId() + "/new", notification);
                notifications.add(notification);
//            }

        }
        return notifications;

    }

    public Boolean checkUserIsInOtherTourProgress(Long userId){
        User user = userService.findById(userId);
        List<Room> rooms = roomRepository.findByRoomUser(user);
        if(rooms.size() > 0){
            for(Room room : rooms){
                if(room.getRoomTour().getIsProgress()){
                    throw new BadRequestException(user.getFullName() + "đang có một tour khác đang diễn ra");
                }
            }
        }
        return false;
    }

    public Tour beginTour(Long tourId, Long userId){
        List<Tour> tours = getListTourByUserId(userId);
        if(tours.size() == 0){
            return null;
        }
        for (Tour tour: tours){
            if(tour.getIsProgress()!=null && tour.getIsProgress()){
                throw new BadRequestException("Bạn đang có tour khác diễn ra");
            }
        }


        Tour newTour = null;
        for (Tour tour: tours){
            if(tour.getId() == tourId){
                newTour = tour;break;
            }
        }

        if(newTour == null){
            throw new NotFoundException("Không tìm thấy tour");
        }

        for(Room room : newTour.getRooms()){
            checkUserIsInOtherTourProgress(room.getRoomUser().getId());
        }

        newTour.setIsProgress(true);
        return tourRepository.save(newTour);
    }

    public Tour endTour(Long tourId, Long userId){
        Tour tour = findById(tourId);
        if(!tour.getIsProgress()){
            throw new BadRequestException("Tour này không diễn ra");
        }
        tour.setIsProgress(false);
        return tourRepository.save(tour);
    }

    public Tour getTourById(Long id,Long userId){
        Tour tour = findById(id);
        for(Room room:tour.getRooms()){
            if(room.getRoomUser().getId() == userId){
                return tour;
            }
        }
        throw new BadRequestException("Không thể lấy thông tin tour");
    }

    public Tour createTour(TourDTO tourDTO, Long touristGuideId){
        if(tourDTO.getDestinations() == null || tourDTO.getDestinations().isEmpty() ){
            throw new BadRequestException("Vui lòng bổ sung lịch trình");
        }
        Tour tour = new Tour();
        tour.setName(tourDTO.getName());
        tour.setDescription(tourDTO.getDescription());
        tour.setIsProgress(false);
        List<Destination> list = new ArrayList<>();
        for (DestinationDTO des : tourDTO.getDestinations()){
            Destination  destination = new Destination();
            destination.setName(des.getName());
            destination.setAddress(des.getAddress());
            destination.setContent(des.getContent());
            destination.setTour(tour);
            destination.setDepartureTime(des.getDepartureTime());
            list.add(destination);
        }
        tour.setDestinations(list);

        Tour newTour = tourRepository.save(tour);
        roomService.createRoom(touristGuideId, tour);
//        List<Long> userIds = new ArrayList<>();
//        userIds.add(touristGuideId);
//        addMembers(newTour.getId(), new MemberDTO(userIds));
        return newTour;
    }

    @Transactional
    public void deleteOldListDes(Tour tour){
        if(tour.getDestinations() != null && tour.getDestinations().size() > 0){
            destinationRepository.deleteAllByTour(tour);
        }
    }

    public List<Destination> buidListDes(List<DestinationDTO> destinationDTOS, Tour tour){
        if(destinationDTOS == null) return null;
        List<Destination> list = new ArrayList<>();
        for (DestinationDTO des : destinationDTOS){
            Destination  destination = new Destination();
            destination.setName(des.getName());
            destination.setAddress(des.getAddress());
            destination.setContent(des.getContent());
            destination.setTour(tour);
            if(destination.getDepartureTime()!= null && destination.getDepartureTime().isBefore(LocalDateTime.now())){
//                throw new BadRequestException("Thời gian phải lớn hơn giờ hiện tại");
            }
            destination.setDepartureTime(des.getDepartureTime());
            list.add(destination);
        }
        return list;
    }

    public Tour findById(Long id){
        Optional<Tour> opTour = tourRepository.findById(id);
        if(opTour.isEmpty()){
            throw new NotFoundException("Không tìm thấy tour");
        }
        return opTour.get();
    }

    @Transactional
    public Tour updateTour(Long id, TourDTO tourDTO){
        Tour tour = findById(id);

        if(!tourDTO.getName().isEmpty() && !tourDTO.getName().equals(tour.getName())){
            tour.setName(tourDTO.getName());
        }

        if(!tourDTO.getDescription().isEmpty() && !tourDTO.getDescription().equals(tour.getDescription())){
            tour.setDescription(tourDTO.getDescription());
        }

        if(tourDTO.getDestinations() != null && !tourDTO.getDestinations().isEmpty()){

            deleteOldListDes(tour);
            List<Destination> list = buidListDes(tourDTO.getDestinations(), tour);
            tour.setDestinations(list);

//            for(DestinationDTO des : tourDTO.getDestinations()){
//                Destination destination = new Destination(des.getId(), des.getName(), des.getAddress(), des.getContent(),  des.getDepartureTime());
//                updateDestination(tour, destination);
//            }
        }


        return tourRepository.save(tour);

    }

    public void updateDestination(Tour tour, Destination destination){
        LocalDateTime dateTime = LocalDateTime.now();
        for(Destination des: tour.getDestinations()){
            if(des.getId().equals(destination.getId())){

                if(destination.getName() != null && !destination.getName().equals(des.getName())){
                    des.setName(destination.getName());
                }
                if(destination.getAddress() != null && !destination.getAddress().equals(des.getAddress())){
                    des.setAddress(destination.getAddress());
                }
                if(destination.getContent() != null && !destination.getContent().equals(des.getContent())){
                    des.setContent(destination.getContent());
                }

                if(destination.getDepartureTime() != null){
                    des.setDepartureTime(destination.getDepartureTime());
                }
//               if(des.getDepartureTime().isBefore(LocalDateTime.now())){
//                   des.setDepartureTime(destination.getDepartureTime());
//               }else {
//                   throw new BadRequestException("Không thể chỉnh sửa");
//               }
                destinationRepository.save(des);
            }
        }
    }

    public boolean deleteTour(Long id){
        Tour tour = findById(id);
        if(tour.getRooms().size() >0){
           throw new BadRequestException("Không thể xóa tour");
        }

         tourRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Tour addMembers(Long tourId, MemberDTO memberDTO){
        if(memberDTO.getUserIds() == null || memberDTO.getUserIds().size() <1){
            throw new BadRequestException("Vui lòng cung cấp đủ dữ liệu");
        }
        Tour tour = findById(tourId);
        for(Long userId: memberDTO.getUserIds()){
            for(Room oldRoom : tour.getRooms()){
                if(oldRoom.getRoomUser().getId() == userId){
                    throw new BadRequestException(oldRoom.getRoomUser().getFullName() +" đã ở trong phòng");
                }
            }
            Room room = roomService.createRoom(userId, tour);
            tour.getRooms().add(room);
        }
        return tour;
    }

    public boolean checkUserIsInTour(Tour tour, User user){
        if(tour.getRooms() != null){
            for(Room room: tour.getRooms()){
                System.out.println("room " + room.getRoomUser().getId() + " " + user.getId());
                if(room.getRoomUser().getId() == user.getId()){
                    return  true;
                }
            }
        }
        return false;
    }

    public Tour getTourProgress(Long userId){
            List<Tour> tours = new ArrayList<>();
            List<Room> rooms = roomService.getListRoomByUserId(userId);
//        System.out.println("ffff" + rooms.size());
            if(rooms.size() != 0){
                for (Room room : rooms){
                    if(room.getRoomTour().getIsProgress()!= null && room.getRoomTour().getIsProgress()){
                        return room.getRoomTour();
                }
            }


        }
            return null;
    }

    public Tour joinRoom(Long tourId, Long userId){
        Tour tour = findById(tourId);
        for(Room roomTour : tour.getRooms()){
           if(roomTour.getRoomUser().getId() == userId){
               if(roomTour.getIsApproved()){
                   throw new BadRequestException("Bạn đã vô phòng");
               }
               throw new BadRequestException("Bạn đã xin vô phòng");
           }
        }
        Room room = roomService.joinRoom(userId, tour);
        tour.getRooms().add(room);
        return tour;
    }

    @Transactional
    public Tour approveMember(Long tourId, MemberDTO memberDTO){
        if(memberDTO.getUserIds() == null || memberDTO.getUserIds().size() <1){
            throw new BadRequestException("Vui lòng cung cấp đủ dữ liệu");
        }

        Tour tour = findById(tourId);
        List<Room> rooms = tour.getRooms();
        for(Long userId: memberDTO.getUserIds()){
            for(Room roomTour: rooms){
                if(roomTour.getRoomUser().getId() == userId){
                    roomTour.setIsApproved(true);break;
                }
            }
        }
        tour.setRooms(rooms);
        return tourRepository.save(tour);
    }

    public List<UserDTO> getListMemberRequestJoin(Long tourId){
        Tour tour = findById(tourId);
        List<UserDTO> users = new ArrayList<>();
        if(tour.getRooms() != null){
            for(Room room : tour.getRooms()){
                if(!room.getIsApproved()){
                    users.add(userService.buildUserDTO(room.getRoomUser()));
                }
            }
        }
        return users;
    }

    public Tour outRoom(Long tourId, Long userId){
        Tour tour = findById(tourId);
        long roomId = 0;
        for(int i=0; i<tour.getRooms().size();i++){
            if(tour.getRooms().get(i).getRoomUser().getId() == userId){
                roomId = tour.getRooms().get(i).getId();
                tour.getRooms().remove(i);
                break;
            }
        }
        Tour newTour=  tourRepository.save(tour);
        roomService.deleteMember(roomId);
        return newTour;
    }

    public Tour removeMembers(Long tourId, MemberDTO memberDTO){
        Tour tour = findById(tourId);
        Tour newTour = tour;
        for(Long userId:memberDTO.getUserIds()){
            newTour = outRoom(tourId, userId);
        }
        return newTour;
    }

    public List<User> getMembers(Long id){
        Tour tour = findById(id);
        List<User> users = new ArrayList<>();
        for(Room room:tour.getRooms()){
            if(room.getIsApproved()){
                users.add(room.getRoomUser());

            }
        }
        return users;
    }


    public List<Destination> getDestinations(Long id){
        Tour tour = findById(id);
        return tour.getDestinations();

    }
}