package com.batuhanseyrek.rezarvasyonSistemi.service.user;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.request.ReservationRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.*;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Employee;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.Reservation;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.User;
import com.batuhanseyrek.rezarvasyonSistemi.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoreService {
    @Autowired
    public AdminRepository adminRepository;
    @Autowired
    public ChairRepository chairRepository;
    @Autowired
    public EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    public List<DtoAdminFull> storeAll() {
        List<Admin> admins = adminRepository.findAll();
        List<Chair> chairs = chairRepository.findAll();
        List<Employee> employees = employeeRepository.findAll();

        List<DtoAdminFull> result = new ArrayList<>();

        for (Admin admin : admins) {
            List<DtoChair> adminChairs = chairs.stream()
                    .filter(c -> c.getAdmin() != null && c.getAdmin().getId().equals(admin.getId()))
                    .map(DtoConverter::toDto)
                    .toList();

            List<DtoEmployee> adminEmployees = employees.stream()
                    .filter(e -> e.getAdmin() != null && e.getAdmin().getId().equals(admin.getId()))
                    .map(DtoConverter::toDto)
                    .toList();

            DtoAdmin dtoAdmin = DtoConverter.toDto(admin);

            result.add(new DtoAdminFull(dtoAdmin, adminChairs, adminEmployees));
        }

        return result;
    }

    public ReservationResponse rezrvationAdd(ReservationRequest request, HttpServletRequest httpRequest){
        Object attr = httpRequest.getAttribute("userId");
        User user= userRepository.findById((Long) attr).orElseThrow(() -> new RuntimeException("user bulunamadı"));
        Optional<Chair> chairOpt = chairRepository.findById(request.getChairId());
        if (chairOpt.isEmpty()) {
            throw new RuntimeException("chair bulunamadı");
        }
        Chair chair = chairOpt.get();
        LocalTime openingTime= chair.getOpeningTime().toLocalTime();
        LocalTime closingTime=chair.getClosingTime().toLocalTime();
        LocalTime startTime= request.getStartTime();
        int durationInMinutes = (int)(chair.getIslemSuresi().toLocalTime().toSecondOfDay()/60);
        LocalTime endTime=startTime.plusMinutes(durationInMinutes);
        if (startTime.isBefore(openingTime) || endTime.isAfter(closingTime)) {
            throw new IllegalArgumentException("Rezervasyon süresi sandalye çalışma saatleri dışında.");
        }
        List<Reservation> conflicts=reservationRepository.findConflictingReservations(  chair.getId(), startTime, endTime);
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Bu saatlerde başka rezervasyon var.");
        }
        Reservation reservation = new Reservation();
        reservation.setChair(chair);
        reservation.setUser(user);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);


        Reservation saved = reservationRepository.save(reservation);
        return DtoConverter.toDto(saved);
    }
    public List<ReservationResponse> reservationList(){
         List<Reservation> reservation = reservationRepository.findAll();

         return reservation.stream().map(DtoConverter::toDto).collect(Collectors.toList());
    }
    public void reservationDelete(Long id){
        reservationRepository.deleteById(id);
    }
    public ReservationResponse reservationUpdate(ReservationResponse request,Long id,HttpServletRequest httpServletRequest){
        Object attr = httpServletRequest.getAttribute("userId"); // <-- Interceptor'dan gelen değer
        User userdb = userRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Admin bulunamadı"));
        int userId=userdb.getId();;
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezervasyon bulunamadı"));

        Chair chair = chairRepository.findByChairName(request.getChairName())
                .orElseThrow(() -> new RuntimeException("Koltuk bulunamadı"));

        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
if (user.getId().equals(userId)){
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setChair(chair);
        reservation.setUser(user);
        reservationRepository.save(reservation);
        return DtoConverter.toDto(reservation);
}
        throw new RuntimeException("Bu rezervasyonu güncelleme yetkiniz yok.");
    }
    public List<ReservationResponse> userReservationGet(HttpServletRequest httpServletRequest){
        Object attr = httpServletRequest.getAttribute("userId"); // <-- Interceptor'dan gelen değer
        User user = userRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Admin bulunamadı"));
        int userId=user.getId();
        List<Reservation> reser=new ArrayList<>();
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation: reservations){
            if (reservation.getUser().getId().equals(userId)){
                reser.add(reservation);
            }
        }
        return reser.stream().map(DtoConverter::toDto).collect(Collectors.toList());
    }
    public void userReservationDelete(HttpServletRequest httpServletRequest,Long id){
        Object attr = httpServletRequest.getAttribute("userId"); // <-- Interceptor'dan gelen değer
        User user = userRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Admin bulunamadı"));
        Optional<Reservation> reservationOpt= reservationRepository.findById(id);
        Reservation reservation=reservationOpt.get();
        if (user.getUserName().equals(reservation.getUser().getUserName())){
            reservationRepository.deleteById(id);
        }
    }
    public Map<LocalTime, Boolean> getAvailableSlots(HttpServletRequest httpServletRequest, Long id) {
        Optional<Chair> chair=chairRepository.findById(id);
        Time islemSuresiSql = chair.get().getIslemSuresi();
        LocalTime islemSuresi = islemSuresiSql.toLocalTime();


        LocalTime openingTime = chair.get().getOpeningTime().toLocalTime();
        LocalTime closingTime = chair.get().getClosingTime().toLocalTime();

        List<Reservation> chairReservations = reservationRepository.findByChair(chair.orElse(null));

        Map<LocalTime, Boolean> availabilityMap = new LinkedHashMap<>();

        for (LocalTime time = openingTime;
             time.plusHours(islemSuresi.getHour()).plusMinutes(islemSuresi.getMinute()).isBefore(closingTime.plusSeconds(1));
             time = time.plusHours(islemSuresi.getHour()).plusMinutes(islemSuresi.getMinute())) {

            LocalTime slotStart = time;
            LocalTime slotEnd = time.plusHours(islemSuresi.getHour()).plusMinutes(islemSuresi.getMinute());

            boolean isAvailable = chairReservations.stream().noneMatch(res ->
                    (res.getStartTime().isBefore(slotEnd) && res.getEndTime().isAfter(slotStart))
            );

            availabilityMap.put(slotStart, isAvailable);
        }

        return availabilityMap;
    }
}
