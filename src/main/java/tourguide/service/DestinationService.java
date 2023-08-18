package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import tourguide.exception.NotFoundException;
import tourguide.model.Destination;
import tourguide.payload.DestinationDTO;
import tourguide.repository.DestinationRepository;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {
    @Autowired
    DestinationRepository  destinationRepository;

    public List<Destination> findScheduleDestination() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        List<Destination>  destinations = destinationRepository.findByDepartureTimeGreaterThan(LocalDateTime.now());
        List<Destination> destinationList = new ArrayList<>();
        for(Destination destination: destinations){
            Duration duration = Duration.between(destination.getDepartureTime(), LocalDateTime.now());
            if(duration.toMinutes() >= -5 && destination.getTour().getIsProgress()){
                destinationList.add(destination);

            }

//            System.out.println(" duration +" + duration.toMinutes() + " "+ formatter.format(destination.getDepartureTime()) );
        }
        return destinationList;
    }

    public Destination findById(Long id){
        Optional<Destination> optionalDestination = destinationRepository.findById(id);
        if(optionalDestination.isEmpty()){
            throw new NotFoundException(
                    "Không tìm thấy điểm đến"
            );
        }
        return optionalDestination.get();
    }

    public Destination updateDestination(DestinationDTO destinationDTO, Long id){
        Destination destination = findById(id);
        if(destinationDTO.getName() != null && !destinationDTO.getName().equals(destination.getName())){
            destination.setName(destinationDTO.getName());
        }
        if(destinationDTO.getAddress() != null && !destinationDTO.getAddress().equals(destination.getAddress())){
            destination.setAddress(destinationDTO.getAddress());
        }
        if(destinationDTO.getContent() != null && !destinationDTO.getContent().equals(destination.getContent())){
            destination.setContent(destinationDTO.getContent());
        }

        if(destinationDTO.getDepartureTime() != null && destinationDTO.getDepartureTime().isBefore(LocalDateTime.now())){
//                   des.setDepartureTime(destination.getDepartureTime());
               }
        return destinationRepository.save(destination);
    }
}