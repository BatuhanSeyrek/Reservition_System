package com.batuhanseyrek.rezarvasyonSistemi.service.user;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoUser;
import com.batuhanseyrek.rezarvasyonSistemi.dto.request.AuthRequest;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.User;
import com.batuhanseyrek.rezarvasyonSistemi.repository.UserRepository;
import com.batuhanseyrek.rezarvasyonSistemi.security.JwtUtil;
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
public class UserService {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public User save(User newUser){
       return userRepository.save(newUser);

    }
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
    public ResponseEntity<String> register(DtoUser request){
        if (userRepository.findByUserName(request.getUserName()).isPresent()){
            return ResponseEntity.badRequest().body("Bu kullanıcı adı zaten var");
        }
        User newUser = new User();
        newUser.setUserName(request.getUserName());
        newUser.setPassword(passwordEncoder.encode( request.getPassword()));
        return ResponseEntity.ok().body(String.valueOf(userRepository.save(newUser))) ;
    }
    public List<User> userList(){
       return userRepository.findAll();
    }
    public User userUpdate(Long id, User user){
        Optional<User> user1 = userRepository.findById(id);
        if (user1.isPresent()) {
            User existingUser = user1.get();
            existingUser.setUserName(user.getUserName());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(existingUser);
        } else {
            // User bulunamadığında ne yapılacağına karar ver
            throw new RuntimeException("User not found");
        }
    }
    public ResponseEntity<?> userDelete(Long id){
        userRepository.deleteById(id);
        return ResponseEntity.ok("Silme işleme başarılı...");
    }
}
