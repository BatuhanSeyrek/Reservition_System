package com.batuhanseyrek.rezarvasyonSistemi.controller.user;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.request.ReservationRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdminFull;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoChair;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.ReservationResponse;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.repository.ChairRepository;
import com.batuhanseyrek.rezarvasyonSistemi.service.user.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/store")
public class ReservationController {
    @Autowired
    public ReservationService storeService;
    @Autowired
    private ChairRepository chairRepository;

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
public List<Map<String, Object>> getAvailableSlots(@PathVariable Long id) {
        return storeService.getAvailableSlots(id);
}
    @GetMapping("/chairgetbystore/{storeId}")
    public List<DtoChair> getChairsByStore(@PathVariable Long storeId) {
        return storeService.getChairsByStore(storeId);
    }

    // Son 1 aya ait rezervasyonları JSON olarak döndür
    @GetMapping("/getMyReservations")
    public List<ReservationResponse> getMyReservations(HttpServletRequest request) {
        return storeService.getReservationsForAdmin(request);
    }

    // Son 6 ayı Excel olarak indir
    @GetMapping("/exportReservations")
    public ResponseEntity<byte[]> exportReservations(HttpServletRequest request) throws IOException {
        byte[] excelData = storeService.exportReservationsToExcel(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reservations.xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }
}
