package com.batuhanseyrek.rezarvasyonSistemi.service.user.Impl;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.request.ReservationRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.*;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Employee;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.Reservation;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.User;
import com.batuhanseyrek.rezarvasyonSistemi.repository.*;
import com.batuhanseyrek.rezarvasyonSistemi.service.user.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements StoreService {
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
    @Override
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
    @Override
    public ReservationResponse rezrvationAdd(ReservationRequest request, HttpServletRequest httpRequest){
        Object attr = httpRequest.getAttribute("userId");
        User user= userRepository.findById((Long) attr).orElseThrow(() -> new RuntimeException("user bulunamadı"));
        Optional<Chair> chairOpt = chairRepository.findById(request.getChairId());
        if (chairOpt.isEmpty()) {
            throw new RuntimeException("chair bulunamadı");
        }
        Chair chair = chairOpt.get();

        LocalDate reservationDate = request.getReservationDate(); // gelen tarih
        LocalDate today = LocalDate.now();

        // Tarih kontrolü: geçmiş tarih veya 1 hafta sonrası sınırı
        if (reservationDate.isBefore(today)) {
            throw new IllegalArgumentException("Rezervasyon tarihi geçmiş olamaz.");
        }
        if (reservationDate.isAfter(today.plusWeeks(1))) {
            throw new IllegalArgumentException("Rezervasyon sadece 1 hafta sonraya kadar yapılabilir.");
        }

        LocalTime openingTime= chair.getOpeningTime().toLocalTime();
        LocalTime closingTime=chair.getClosingTime().toLocalTime();
        LocalTime startTime= request.getStartTime();
        int durationInMinutes = (int)(chair.getIslemSuresi().toLocalTime().toSecondOfDay()/60);
        LocalTime endTime=startTime.plusMinutes(durationInMinutes);

        if (startTime.isBefore(openingTime) || endTime.isAfter(closingTime)) {
            throw new IllegalArgumentException("Rezervasyon süresi sandalye çalışma saatleri dışında.");
        }

        // Aynı tarih ve zaman aralığında çakışan rezervasyonlar kontrolü (burada date parametresini de kullan)
        List<Reservation> conflicts = reservationRepository.findConflictingReservationsByDate(chair.getId(), reservationDate, startTime, endTime);
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Bu saatlerde başka rezervasyon var.");
        }

        Reservation reservation = new Reservation();
        reservation.setChair(chair);
        reservation.setUser(user);
        reservation.setReservationDate(reservationDate); // rezervasyon tarihini set et
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setReminderSent(false);
        Reservation saved = reservationRepository.save(reservation);
        return DtoConverter.toDto(saved);
    }
    @Override
    public List<ReservationResponse> reservationList(){
         List<Reservation> reservation = reservationRepository.findAll();

         return reservation.stream().map(DtoConverter::toDto).collect(Collectors.toList());
    }
    @Override
    public void reservationDelete(Long id){
        reservationRepository.deleteById(id);
    }
    @Override
    public ReservationResponse reservationUpdate(ReservationResponse request, Long id, HttpServletRequest httpServletRequest) {
        // 1. Kullanıcıyı Interceptor'dan al
        Object attr = httpServletRequest.getAttribute("userId");
        User currentUser = userRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // 2. Güncellenecek rezervasyonu bul
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezervasyon bulunamadı"));

        // 3. Eğer rezervasyon sahibi değilse ve admin değilse izin verme
        // Burada admin kontrolü de eklemek isteyebilirsin, örn: currentUser.isAdmin()
        if (!reservation.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bu rezervasyonu güncelleme yetkiniz yok.");
        }

        // 4. Yeni sandalye ve kullanıcı bilgilerini al (varsa güncelle)
        Chair chair = chairRepository.findByChairName(request.getChairName())
                .orElseThrow(() -> new RuntimeException("Koltuk bulunamadı"));

        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // 5. Rezervasyon tarih ve saatleri doğrulaması yap (örn: çalışma saatleri, süre, çakışma kontrolü)
        LocalDate reservationDate = request.getReservationDate();  // request'te tarih var mı kontrol et
        LocalTime startTime = request.getStartTime();
        int durationInMinutes = (int) (chair.getIslemSuresi().toLocalTime().toSecondOfDay() / 60);
        LocalTime endTime = startTime.plusMinutes(durationInMinutes);

        LocalTime openingTime = chair.getOpeningTime().toLocalTime();
        LocalTime closingTime = chair.getClosingTime().toLocalTime();

        if (startTime.isBefore(openingTime) || endTime.isAfter(closingTime)) {
            throw new IllegalArgumentException("Rezervasyon süresi sandalye çalışma saatleri dışında.");
        }

        // Çakışma kontrolü (tarihe göre de kontrol et)
        List<Reservation> conflicts = reservationRepository.findConflictingReservationsByDate(
                chair.getId(),
                reservationDate,
                startTime,
                endTime);

        // Kendini hariç tut
        conflicts.removeIf(r -> r.getId().equals(reservation.getId()));

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Bu saatlerde başka rezervasyon var.");
        }

        // 6. Rezervasyonu güncelle
        reservation.setChair(chair);
        reservation.setUser(user);
        reservation.setReservationDate(reservationDate);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);

        // 7. Kaydet ve DTO'ya dönüştür
        Reservation saved = reservationRepository.save(reservation);
        return DtoConverter.toDto(saved);
    }
    @Override
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
    @Override
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
    @Override
    public Map<LocalDate, Map<LocalTime, Boolean>> getAvailableSlots(HttpServletRequest httpServletRequest, Long id) {
        Optional<Chair> chairOpt = chairRepository.findById(id);
        if (chairOpt.isEmpty()) {
            throw new RuntimeException("Chair not found");
        }
        Chair chair = chairOpt.get();

        LocalTime islemSuresi = chair.getIslemSuresi().toLocalTime();
        LocalTime openingTime = chair.getOpeningTime().toLocalTime();
        LocalTime closingTime = chair.getClosingTime().toLocalTime();

        Map<LocalDate, Map<LocalTime, Boolean>> result = new LinkedHashMap<>();

        LocalDate today = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = today.plusDays(i);
            List<Reservation> reservations = reservationRepository.findByChairAndReservationDate(chair, currentDate);

            Map<LocalTime, Boolean> availabilityMap = new LinkedHashMap<>();

            for (LocalTime time = openingTime;
                 time.plusHours(islemSuresi.getHour()).plusMinutes(islemSuresi.getMinute()).isBefore(closingTime.plusSeconds(1));
                 time = time.plusHours(islemSuresi.getHour()).plusMinutes(islemSuresi.getMinute())) {

                LocalTime slotStart = time;
                LocalTime slotEnd = time.plusHours(islemSuresi.getHour()).plusMinutes(islemSuresi.getMinute());

                boolean isAvailable = reservations.stream().noneMatch(res ->
                        (res.getStartTime().isBefore(slotEnd) && res.getEndTime().isAfter(slotStart))
                );

                availabilityMap.put(slotStart, isAvailable);
            }

            result.put(currentDate, availabilityMap);
        }

        return result;
    }

}
