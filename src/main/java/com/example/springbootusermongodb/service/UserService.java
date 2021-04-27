package com.example.springbootusermongodb.service;

import com.example.springbootusermongodb.aop.InvalidInputException;
import com.example.springbootusermongodb.aop.UserNotFoundException;
import com.example.springbootusermongodb.dto.UserPatch;
import com.example.springbootusermongodb.model.User;
import com.example.springbootusermongodb.repository.UserRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll(User.class);
    }

    public User getUserByUserId(String userId) throws Exception {
        User user = userRepository.findById(userId, User.class);
        if (user == null) {
            throw new UserNotFoundException("User not Found.");
        }
        return user;
    }

    public User addNewUser(User user) throws Exception {
        nullCheck(user);
        return userRepository.save(user);
    }

    private void nullCheck(User user) throws InvalidInputException {
        if (user.getUserId() == null || user.getName() == null || user.getEmail() == null) {
            throw new InvalidInputException("Input field is required.");
        }
        if (user.getUserId().equals("") || user.getName().equals("") || user.getEmail().equals("")) {
            throw new InvalidInputException("Input is empty");
        }
    }

    public User updateUserByUserId(User user, String userId) throws Exception {
        nullCheck(user);
        User userUpdated = userRepository.findById(userId, User.class);
        if (userUpdated == null) {
            throw new UserNotFoundException("User not Found");
        }
        userUpdated.setUserId(user.getUserId());
        userUpdated.setName(user.getName());
        userUpdated.setEmail(user.getEmail());
        return userRepository.save(userUpdated);
    }

    public String updateUserByAction(ArrayList<UserPatch> userPatch, String userId) throws Exception {
        if (userPatch.isEmpty()) {
            throw new InvalidInputException("Input is empty");
        }
        User user = userRepository.findById(userId, User.class);
        if (user == null) {
            throw new UserNotFoundException("User not Found.");
        }
        for (UserPatch userPatch1 : userPatch) {
            patchNullCheck(userPatch1);
            if (userPatch1.getAction().equals("add") || userPatch1.getAction().equals("replace")) {
                patchActionForReplace(userPatch1, userId);
            } else if (userPatch1.getAction().equals("delete")) {
                patchActionForDelete(userPatch1, userId);
            } else {
                throw new InvalidInputException("Action is invalid");
            }
        }
        return "Patched Successfully";
    }

    private void patchNullCheck(UserPatch userPatch1) throws Exception {
        if (userPatch1.getAction() == null || userPatch1.getFieldName() == null) {
            throw new InvalidInputException("Input field is required.");
        }
        if (userPatch1.getAction().equals("") || userPatch1.getFieldName().equals("")) {
            throw new InvalidInputException("Input is empty");
        }
    }

    private void patchActionForDelete(UserPatch userPatch1, String userId) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("_id").is(userId));
        update.unset(userPatch1.getFieldName());
        userRepository.updateFirst(query, update, User.class);
    }

    private void patchActionForReplace(UserPatch userPatch1, String userId) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("_id").is(userId));
        update.set(userPatch1.getFieldName(), userPatch1.getValue());
        userRepository.updateFirst(query, update, User.class);
    }

    public String deleteUserByUserId(String userId) throws Exception {
        User user = userRepository.findById(userId, User.class);
        if (user == null) {
            throw new UserNotFoundException("User not Found.");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userId));
        userRepository.remove(query, User.class);
        return "User deleted Successfully";
    }
}
