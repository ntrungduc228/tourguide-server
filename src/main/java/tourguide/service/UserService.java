package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tourguide.exception.BadRequestException;
import tourguide.exception.NotFoundException;
import tourguide.model.Comment;
import tourguide.model.Role;
import tourguide.model.User;
import tourguide.payload.AuthDTO;
import tourguide.payload.CommentDTO;
import tourguide.payload.UserDTO;
import tourguide.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public UserDTO buildUserDTO(User user){
        UserDTO userDTO = new UserDTO().builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .build();
        return userDTO;
    }

    public UserDTO getInfo(Long id){
        User user = findById(id);
       return buildUserDTO(user);
    }

    public User findById(Long id){
        Optional<User> opUser = userRepository.findById(id);
        if(opUser.isEmpty()){
            throw new NotFoundException("Không tìm thấy người dùng");
        }
        return opUser.get();
    }

    public boolean checkEmailExist(String email){
        Optional<User> opUser = userRepository.findByEmail(email);
        return opUser.isPresent() ? true : false;
    }

    public UserDTO signUp(AuthDTO authDTO){
        User user = new User();
        user.setEmail(authDTO.getEmail());
        user.setPassword(passwordEncoder.encode(authDTO.getPassword()));
        user.setRole(Role.TOURIST);
        user.setFullName(authDTO.getFullName());
        user.setPhone(authDTO.getPhone());
        user.setAddress(authDTO.getAddress());
        User newUser= userRepository.save(user);
        UserDTO userDTO = new UserDTO().builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
        return userDTO;
    }

    public User findByEmail(String email) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty() ){
            throw new NotFoundException("user not found");
        }
        return user.get();
    }

    public List<User> findByPhone(String phone, Long userId) throws Exception {
        List<User> users = userRepository.findByPhoneStartingWith(phone);
        if(users.size()==0 ){
            return new ArrayList<>();
        }
        List<User> userReturn = new ArrayList<>();
        for(User user: users){
            if(user.getId() != userId){
                userReturn.add(user);
            }
        }
        return userReturn;
    }


    public UserDTO updateProfile(UserDTO userDTO){
        User user = findById(userDTO.getId());
        user.setAddress(userDTO.getAddress());
        if(!userDTO.getPhone().isEmpty()){
            user.setPhone(userDTO.getPhone());
        }

        user.setFullName(userDTO.getFullName());
        user.setAvatar(userDTO.getAvatar());
        User newUser = userRepository.save(user);
        return buildUserDTO(newUser);
    }

}