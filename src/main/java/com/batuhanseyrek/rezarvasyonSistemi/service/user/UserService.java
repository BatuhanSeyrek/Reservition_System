package com.batuhanseyrek.rezarvasyonSistemi.service.user;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoUser;
import com.batuhanseyrek.rezarvasyonSistemi.dto.request.AuthRequest;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    User myApp(HttpServletRequest httpServletRequest);
    User save(User newUser);
    Map<String,Object> mapping(AuthRequest request);
    ResponseEntity<String> register(DtoUser request);
    List<User> userList();
    User userUpdate(HttpServletRequest httpServletRequest, User user);
    ResponseEntity<?> userDelete(Long id);
}
