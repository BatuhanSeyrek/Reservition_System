package com.batuhanseyrek.rezarvasyonSistemi.controller.user;
import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoUser;
import com.batuhanseyrek.rezarvasyonSistemi.dto.request.AuthRequest;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.User;
import com.batuhanseyrek.rezarvasyonSistemi.repository.UserRepository;
import com.batuhanseyrek.rezarvasyonSistemi.security.JwtUtil;
import com.batuhanseyrek.rezarvasyonSistemi.service.user.Impl.UserServiceImpl;
import com.batuhanseyrek.rezarvasyonSistemi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    public UserController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }
@PostMapping("/login")
public ResponseEntity<Map<String,Object>> login(@RequestBody AuthRequest request){
        return  ResponseEntity.ok(userService.mapping(request));
}
@PostMapping("/register")
public ResponseEntity<String> register(@RequestBody() DtoUser request){
        return userService.register(request);
}
@GetMapping("/list")
public List<User> userList(){
    return userService.userList();
}
@PutMapping("/update/{id}")
    public User userUpdate(@PathVariable Long id,@RequestBody User user){
        return userService.userUpdate(id,user);
}
@DeleteMapping("/delete/{id}")
    public void userDelete(@PathVariable Long id){
        userService.userDelete(id);
}
}