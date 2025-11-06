package com.shantanu.journalApp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.shantanu.journalApp.dto.UserDTO;
import com.shantanu.journalApp.entity.User;
import com.shantanu.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> all = userService.getAllEntries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<UserDTO> createNewAdminUser(@RequestBody UserDTO newUserDTO) {
        userService.saveAdmin(newUserDTO);
        return new ResponseEntity<>(newUserDTO, HttpStatus.CREATED);
    }
}
