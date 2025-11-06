package com.shantanu.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import com.shantanu.journalApp.dto.UserDTO;
import com.shantanu.journalApp.entity.User;
import com.shantanu.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //private static final Logger logger = LoggerFactory.getLogger(UserService.class); // used for understanding logging but we can use @Slf4j annotation internally includes instance of logger.


    public boolean saveNewUser(UserDTO userDto) { // used only when new user is saved with credentials
        try {
            // Build User entity from DTO and encode password
            User user = User.builder()
                    .userName(userDto.getUserName())
                    .password(passwordEncoder.encode(userDto.getPassword())) // encode before saving
                    .roles(Arrays.asList("USER")) // default role
                    .email(userDto.getEmail())
                    .sentimentAnalysis(userDto.isSentimentAnalysis())
                    .build();
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("Error occured for {}: ", userDto.getUserName(), e);
            return false;
        }
    }

    // Overload for update(PUT Mapping)
    public boolean saveNewUser(User userEntity) {
        // encode password before saving
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        return true;
    }

    // used for save most of the time
    public void saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Exception : ",e);
        }
    }

    // used when new admin user is saved with credentials
    public void saveAdmin(UserDTO userDto) {
        User user = User.builder()
                .userName(userDto.getUserName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(Arrays.asList("USER", "ADMIN"))
                .email(userDto.getEmail())
                .sentimentAnalysis(userDto.isSentimentAnalysis())
                .build();
        userRepository.save(user);
    }

    public ArrayList<User> getAllEntries() {
        return new ArrayList<>(userRepository.findAll());
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public void deleteByUserName(String userName) {
        userRepository.deleteByUserName(userName);
    }

}
