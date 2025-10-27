package com.vuln.controller;

import com.vuln.entity.User;
import com.vuln.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setUsername(userDetails.getUsername());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPhoneNumber(userDetails.getPhoneNumber());
            existingUser.setFullName(userDetails.getFullName());
            existingUser.setShippingAddress(userDetails.getShippingAddress());
            existingUser.setBillingAddress(userDetails.getBillingAddress());
            existingUser.setIsAdmin(userDetails.getIsAdmin());
            existingUser.setIsActive(userDetails.getIsActive());
            return ResponseEntity.ok(userRepository.save(existingUser));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // 测试端点：获取所有用户信息（包含敏感信息）
    @GetMapping("/sensitive")
    public List<User> getAllUsersWithSensitiveInfo() {
        return userRepository.findAll();
    }
    
    // 测试端点：根据用户名获取用户信息
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
        List<User> users = userRepository.findAll().stream()
            .filter(user -> user.getUsername().contains(keyword) || 
                           user.getFullName().contains(keyword) ||
                           user.getEmail().contains(keyword))
            .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
    
    // 危险端点：获取所有用户敏感信息（用于演示数据泄露）
    @GetMapping("/leak")
    public ResponseEntity<Map<String, Object>> leakSensitiveData() {
        Map<String, Object> leakData = new HashMap<>();
        
        // 获取所有用户信息
        List<User> allUsers = userRepository.findAll();
        leakData.put("users", allUsers);
        
        // 添加系统信息
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("totalUsers", allUsers.size());
        systemInfo.put("adminUsers", allUsers.stream().filter(User::getIsAdmin).count());
        systemInfo.put("activeUsers", allUsers.stream().filter(User::getIsActive).count());
        systemInfo.put("timestamp", java.time.LocalDateTime.now());
        
        leakData.put("systemInfo", systemInfo);
        
        // 添加统计信息
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("userCount", allUsers.size());
        statistics.put("adminCount", allUsers.stream().filter(User::getIsAdmin).count());
        statistics.put("averageUsersPerDay", allUsers.size() / 30.0); // 假设30天
        
        leakData.put("statistics", statistics);
        
        return ResponseEntity.ok(leakData);
    }
}
