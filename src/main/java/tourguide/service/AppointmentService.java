package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourguide.exception.NotFoundException;
import tourguide.model.Appointment;
import tourguide.repository.AppointmentRepository;

import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;

    public Appointment findById(Long id){
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if(optionalAppointment.isEmpty()){
            throw new NotFoundException("Không tìm thấy điểm hẹn");
        }
        return optionalAppointment.get();
    }
}