package com.batuhanseyrek.rezarvasyonSistemi.controller.user;

import com.batuhanseyrek.rezarvasyonSistemi.dto.request.ReservationRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdminFull;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.ReservationResponse;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.Reservation;
import com.batuhanseyrek.rezarvasyonSistemi.service.user.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {
    @Autowired
    public StoreService storeService;

    @GetMapping(path = "/storeAll")
    public List<DtoAdminFull> storeAll(){

        return ResponseEntity.ok(storeService.storeAll()).getBody();
    }
    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request,
                                                    HttpServletRequest httpRequest) {

        return ResponseEntity.ok(storeService.rezrvationAdd(request,httpRequest));
}
@GetMapping("/reservationList")
    public  List<ReservationResponse> reservationList(){
        return storeService.reservationList();
}
@DeleteMapping("/reservationDelete/{id}")
    public void reservationDelete(@PathVariable Long id){
        storeService.reservationDelete(id);
}
@PutMapping("/reservationUpdate/{id}")
    public ReservationResponse reservationUpdate(@RequestBody ReservationResponse reservation,@PathVariable Long id,HttpServletRequest httpServletRequest){
       return storeService.reservationUpdate(reservation,id,httpServletRequest);
}
@GetMapping("/userReservationGet")
    public List<ReservationResponse> userReservationGet(HttpServletRequest httpServletRequest){
        return storeService.userReservationGet(httpServletRequest);
}
@DeleteMapping("/userReservationDelete/{id}")
    public void userReservationDelete(HttpServletRequest httpServletRequest,@PathVariable Long id){
        storeService.userReservationDelete(httpServletRequest,id);
}
@GetMapping("/getAvailableSlots/{id}")
public Map<LocalTime, Boolean> getAvailableSlots(HttpServletRequest httpServletRequest,@PathVariable Long id) {
        return storeService.getAvailableSlots(httpServletRequest,id);
}
}
