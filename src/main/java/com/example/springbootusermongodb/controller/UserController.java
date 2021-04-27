package com.example.springbootusermongodb.controller;

import com.example.springbootusermongodb.dto.UserPatch;
import com.example.springbootusermongodb.model.User;
import com.example.springbootusermongodb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<User> getUserByUserId(@PathVariable String userId) throws Exception {
        return status(HttpStatus.OK).body(userService.getUserByUserId(userId));
    }

    @PostMapping(value = "/users")
    public ResponseEntity<User> addNewUsers(@RequestBody User user) throws Exception {
        return status(HttpStatus.CREATED).body(userService.addNewUser(user));
    }

    @PutMapping(value = "/users/{userId}")
    public ResponseEntity<User> updateUserByUserId(@RequestBody User user, @PathVariable String userId) throws Exception {
        return status(HttpStatus.OK).body(userService.updateUserByUserId(user, userId));
    }

    @PatchMapping(value = "/users/{userId}")
    public ResponseEntity<String> updateUserByAction(@RequestBody ArrayList<UserPatch> userPatch, @PathVariable String userId) throws Exception {
        return status(HttpStatus.OK).body(userService.updateUserByAction(userPatch, userId));
    }

    @DeleteMapping(value = "/users/{userId}")
    public ResponseEntity<String> deleteUserByUserId(@PathVariable String userId) throws Exception {
        return status(HttpStatus.OK).body(userService.deleteUserByUserId(userId));
    }
}
