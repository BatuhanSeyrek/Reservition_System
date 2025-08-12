package com.batuhanseyrek.rezarvasyonSistemi.service.user.Impl;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.request.ReservationRequest;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.*;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Employee;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Store;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.Reservation;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.User;
import com.batuhanseyrek.rezarvasyonSistemi.repository.*;
import com.batuhanseyrek.rezarvasyonSistemi.service.user.ReservationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @PersistenceContext
    private EntityManager entityManager;
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
        List<DtoAdminFull> result = new ArrayList<>();

        for (Admin admin : admins) {
            Store store = admin.getStore();
            if (store == null) continue;

            List<DtoChair> adminChairs = admin.getChairs().stream()
                    .map(DtoConverter::toDto)
                    .toList();

            List<DtoEmployee> adminEmployees = admin.getEmployees().stream()
                    .map(DtoConverter::toDto)
                    .toList();
            DtoStore adminStore = DtoConverter.toDto(admin.getStore());
            DtoAdmin dtoAdmin = DtoConverter.toDto(admin);

            result.add(new DtoAdminFull(dtoAdmin, adminChairs, adminEmployees,adminStore));
        }

        return result;
    }

    @Override
    public ReservationResponse rezrvationAdd(ReservationRequest request, HttpServletRequest httpRequest) {
        Object attr = httpRequest.getAttribute("userId");
        User user = userRepository.findById((Long) attr).orElseThrow(() -> new RuntimeException("user bulunamadı"));

        Chair chair = chairRepository.findById(request.getChairId())
                .orElseThrow(() -> new RuntimeException("chair bulunamadı"));

        LocalDate reservationDate = request.getReservationDate();
        LocalDate today = LocalDate.now();

        if (reservationDate.isBefore(today)) {
            throw new IllegalArgumentException("Rezervasyon tarihi geçmiş olamaz.");
        }
        if (reservationDate.isAfter(today.plusWeeks(1))) {
            throw new IllegalArgumentException("Rezervasyon sadece 1 hafta sonraya kadar yapılabilir.");
        }

        LocalTime openingTime = chair.getOpeningTime();
        LocalTime closingTime = chair.getClosingTime();
        LocalTime startTime = request.getStartTime();
        int durationInMinutes = (int) (chair.getIslemSuresi().toSecondOfDay() / 60);
        LocalTime endTime = startTime.plusMinutes(durationInMinutes);

        if (startTime.isBefore(openingTime) || endTime.isAfter(closingTime)) {
            throw new IllegalArgumentException("Rezervasyon süresi sandalye çalışma saatleri dışında.");
        }
        List<Reservation> conflicts;

        Reservation reservation = new Reservation();
        reservation.setChair(chair);
        reservation.setUser(user);
        reservation.setStore(chair.getStore());
        reservation.setReservationDate(reservationDate);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setReminderSent(false);
        try{
        Reservation saved = reservationRepository.save(reservation);
        return DtoConverter.toDto(saved);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public List<ReservationResponse> reservationList() {
        return reservationRepository.findAll().stream()
                .map(DtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void reservationDelete(Long id) {
        reservationRepository.deleteById(id);
    }
    @Override
    public List<DtoChair> getChairsByStore(Long storeId) {
        String jpql = "SELECT c FROM Chair c WHERE c.store.id = :storeId";
        List<Chair> chairs = entityManager.createQuery(jpql, Chair.class)
                .setParameter("storeId", storeId)
                .getResultList();

        // Chair listesi DtoChair listesine dönüştürülüyor
        List<DtoChair> dtoChairs = chairs.stream()
                .map(DtoConverter::toDto)
                .collect(Collectors.toList());

        return dtoChairs;
    }
    @Override
    public ReservationResponse reservationUpdate(ReservationResponse request, Long id, HttpServletRequest httpServletRequest) {
        Object attr = httpServletRequest.getAttribute("userId");
        User currentUser = userRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezervasyon bulunamadı"));

        if (!reservation.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bu rezervasyonu güncelleme yetkiniz yok.");
        }

        Chair chair = chairRepository.findById(request.getChairId())
                .orElseThrow(() -> new RuntimeException("Koltuk bulunamadı"));

        User user = userRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        LocalDate reservationDate = request.getReservationDate();
        LocalTime startTime = request.getStartTime();
        int durationInMinutes = (int) (chair.getIslemSuresi().toSecondOfDay() / 60);
        LocalTime endTime = startTime.plusMinutes(durationInMinutes);

        LocalTime openingTime = chair.getOpeningTime();
        LocalTime closingTime = chair.getClosingTime();

        if (startTime.isBefore(openingTime) || endTime.isAfter(closingTime)) {
            throw new IllegalArgumentException("Rezervasyon süresi sandalye çalışma saatleri dışında.");
        }

        List<Reservation> conflicts = reservationRepository.findConflictingReservationsByDate(
                chair.getId(), reservationDate, startTime, endTime);
        conflicts.removeIf(r -> r.getId().equals(reservation.getId()));
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Bu saatlerde başka rezervasyon var.");
        }

        reservation.setChair(chair);
        reservation.setUser(user);
        reservation.setReservationDate(reservationDate);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);

        return DtoConverter.toDto(reservationRepository.save(reservation));
    }

    @Override
    public List<ReservationResponse> userReservationGet(HttpServletRequest httpServletRequest) {
        Object attr = httpServletRequest.getAttribute("userId");
        User user = userRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        return reservationRepository.findAll().stream()
                .filter(res -> res.getUser().getId().equals(user.getId()))
                .map(DtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void userReservationDelete(HttpServletRequest httpServletRequest, Long id) {
        Object attr = httpServletRequest.getAttribute("userId");
        User user = userRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezervasyon bulunamadı"));

        if (user.getId().equals(reservation.getUser().getId())) {
            reservationRepository.deleteById(id);
        } else {
            throw new RuntimeException("Bu rezervasyonu silme yetkiniz yok.");
        }
    }


    @Override
    public List<Map<String, Object>> getAvailableSlots(Long storeId) {
        List<Chair> chairs = chairRepository.findByStoreId(storeId);
        List<Map<String, Object>> resultList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Chair chair : chairs) {
            Map<String, Map<String, Boolean>> dateAvailabilityMap = new LinkedHashMap<>();

            long islemSuresiDakika = chair.getIslemSuresi().getHour() * 60L + chair.getIslemSuresi().getMinute();
            if (islemSuresiDakika <= 0) {
                throw new IllegalArgumentException("İşlem süresi 0 veya negatif olamaz");
            }

            LocalTime openingTime = chair.getOpeningTime();
            LocalTime closingTime = chair.getClosingTime();

            LocalDateTime currentDateTime = LocalDateTime.of(today, openingTime);

            // 7 gün sınırı (bugünden 7 gün sonrası açılış saati)
            LocalDateTime limitDateTime = LocalDateTime.of(today.plusDays(7), openingTime);

            while (currentDateTime.isBefore(limitDateTime)) {
                LocalDate currentDate = currentDateTime.toLocalDate();
                dateAvailabilityMap.putIfAbsent(currentDate.toString(), new LinkedHashMap<>());

                List<Reservation> reservations =
                        reservationRepository.findByChairAndReservationDate(chair, currentDate);

                LocalTime slotStart = currentDateTime.toLocalTime();
                LocalTime slotEnd = slotStart.plusMinutes(islemSuresiDakika);

                // Slot kapanış saatini geçmiyorsa ekle
                if (!slotEnd.isAfter(closingTime)) {
                    boolean isAvailable = reservations.stream().noneMatch(res ->
                            res.getStartTime().isBefore(slotEnd) && res.getEndTime().isAfter(slotStart)
                    );
                    dateAvailabilityMap.get(currentDate.toString()).put(slotStart.toString(), isAvailable);

                    currentDateTime = currentDateTime.plusMinutes(islemSuresiDakika);
                } else {
                    // Gün bitti → ertesi günün açılış saati
                    currentDateTime = LocalDateTime.of(currentDate.plusDays(1), openingTime);
                }
            }

            Map<String, Object> chairInfo = new LinkedHashMap<>();
            chairInfo.put("chairId", chair.getId());
            chairInfo.put("chairName", chair.getChairName());
            chairInfo.put("slots", dateAvailabilityMap);
            resultList.add(chairInfo);
        }

        return resultList;
    }


}
