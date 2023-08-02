package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourguide.exception.BadRequestException;
import tourguide.exception.NotFoundException;
import tourguide.model.Destination;
import tourguide.model.Tour;
import tourguide.payload.DestinationDTO;
import tourguide.payload.TourDTO;
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

    public Tour createTour(TourDTO tourDTO){
        if(tourDTO.getDestinations() == null || tourDTO.getDestinations().isEmpty() ){
            throw new BadRequestException("Vui lòng bổ sung lịch trình");
        }
        Tour tour = new Tour();
        tour.setName(tourDTO.getName());
        tour.setDescription(tourDTO.getDescription());
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

        return tourRepository.save(tour);
    }

    public Tour findById(Long id){
        Optional<Tour> opTour = tourRepository.findById(id);
        if(opTour.isEmpty()){
            throw new NotFoundException("Không tìm thấy tour");
        }
        return opTour.get();
    }

    public Tour updateTour(Long id, TourDTO tourDTO){
        Tour tour = findById(id);

        if(!tourDTO.getName().isEmpty() && !tourDTO.getName().equals(tour.getName())){
            tour.setName(tourDTO.getName());
        }

        if(!tourDTO.getDescription().isEmpty() && !tourDTO.getDescription().equals(tour.getDescription())){
            tour.setDescription(tourDTO.getDescription());
        }

        if(tourDTO.getDestinations() != null && !tourDTO.getDestinations().isEmpty()){
            for(DestinationDTO des : tourDTO.getDestinations()){
                Destination destination = new Destination(des.getId(), des.getName(), des.getAddress(), des.getContent(),  des.getDepartureTime());
                updateDestination(tour, destination);
            }
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
//               if(des.getDepartureTime().isBefore(LocalDateTime.now())){
//                   des.setDepartureTime(destination.getDepartureTime());
//               }else {
//                   throw new BadRequestException("Không thể chỉnh sửa");
//               }

                return;
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

}