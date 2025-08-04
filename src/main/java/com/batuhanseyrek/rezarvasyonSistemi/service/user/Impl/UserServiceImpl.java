package com.batuhanseyrek.rezarvasyonSistemi.service.user.Impl;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoUser;
import com.batuhanseyrek.rezarvasyonSistemi.dto.request.AuthRequest;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.User;
import com.batuhanseyrek.rezarvasyonSistemi.repository.UserRepository;
import com.batuhanseyrek.rezarvasyonSistemi.security.JwtUtil;
import com.batuhanseyrek.rezarvasyonSistemi.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User save(User newUser){
       return userRepository.save(newUser);

    }
    @Override
    public Map<String,Object> mapping(AuthRequest request){
        try{
            authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        }
        catch (Exception e){
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.UNAUTHORIZED.value()); // 401
            errorResponse.put("error", "Hatalı giriş");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse).getBody();
        }
        Optional<User> user = userRepository.findByUserName(request.getUsername());
        String token= jwtUtil.generateToken(request.getUsername());
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("token",token);
        responseMap.put("userName",request.getUsername());
        responseMap.put("id",user.get().getId());
        return responseMap;
    }
    @Override
    public User myApp(HttpServletRequest httpRequest) {
        Object attr = httpRequest.getAttribute("userId"); // <-- Interceptor'dan gelen değer
        User user = userRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Admin bulunamadı"));
        User newuser=new User();
        newuser.setUserName(user.getUserName());
        newuser.setId(user.getId());
        newuser.setPassword(user.getPassword());
        newuser.setEmail(user.getEmail());
        newuser.setPhoneNumber(user.getPhoneNumber());
        newuser.setNotificationType(user.getNotificationType());
        return newuser;
    }
    @Override
    public ResponseEntity<String> register(DtoUser request){
        if (userRepository.findByUserName(request.getUserName()).isPresent()){
            return ResponseEntity.badRequest().body("Bu kullanıcı adı zaten var");
        }
        User newUser = new User();
        newUser.setUserName(request.getUserName());
        newUser.setPassword(passwordEncoder.encode( request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setNotificationType(request.getNotificationType());
        return ResponseEntity.ok().body(String.valueOf(userRepository.save(newUser))) ;
    }
    @Override
    public List<User> userList(){
       return userRepository.findAll();
    }
    @Override
    public User userUpdate(Long id, User user){
        Optional<User> user1 = userRepository.findById(id);
        if (user1.isPresent()) {
            User existingUser = user1.get();
            existingUser.setUserName(user.getUserName());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            existingUser.setEmail(user.getEmail());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setNotificationType(user.getNotificationType());
            return userRepository.save(existingUser);
        } else {
            // User bulunamadığında ne yapılacağına karar ver
            throw new RuntimeException("User not found");
        }
    }
    @Override
    public ResponseEntity<?> userDelete(Long id){
        userRepository.deleteById(id);
        return ResponseEntity.ok("Silme işleme başarılı...");
    }
}
