package com.vehiclerental.service;
import com.vehiclerental.model.User;
import com.vehiclerental.repository.UserRepository;
import com.vehiclerental.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public List<User> getUsersByRole(User.UserRole role) {
        return userRepository.findByRole(role);
    }
    
    public List<User> getUnverifiedOwners() {
        return userRepository.findByIsVerified(false);
    }
    
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setFullName(userDetails.getFullName());
        user.setPhone(userDetails.getPhone());
        return userRepository.save(user);
    }
    
    public User verifyUser(Long id) {
        User user = getUserById(id);
        user.setIsVerified(true);
        return userRepository.save(user);
    }
    
    public User toggleUserStatus(Long id) {
        User user = getUserById(id);
        user.setIsActive(!user.getIsActive());
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
