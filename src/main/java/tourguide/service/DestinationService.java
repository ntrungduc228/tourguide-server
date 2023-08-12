package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourguide.exception.NotFoundException;
import tourguide.model.Destination;
import tourguide.payload.DestinationDTO;
import tourguide.repository.DestinationRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DestinationService {
    @Autowired
    DestinationRepository  destinationRepository;

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