package com.batuhanseyrek.rezarvasyonSistemi.dto;

import com.batuhanseyrek.rezarvasyonSistemi.dto.response.*;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.*;
import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.Reservation;
import org.springframework.stereotype.Service;

@Service
public class DtoConverter {



    public static DtoEmployee toDto(Employee employee) {
        DtoEmployee dto = new DtoEmployee();
        dto.setId(employee.getId());
        dto.setEmployeeName(employee.getEmployeeName());
        dto.setAdminId(employee.getAdmin() != null ? employee.getAdmin().getId() : null);
        dto.setChairId(employee.getChair() != null ? employee.getChair().getId() : null);
        return dto;
    }

    public static DtoChair toDto(Chair chair) {
        DtoChair dto = new DtoChair();
        dto.setId(chair.getId());
        dto.setChairName(chair.getChairName());
        dto.setOpeningTime(chair.getOpeningTime());
        dto.setClosingTime(chair.getClosingTime());
        dto.setIslemSuresi(chair.getIslemSuresi());
        dto.setAdminId(chair.getAdmin() != null ? chair.getAdmin().getId() : null);
        dto.setStoreId(chair.getStore() != null ? chair.getStore().getId() : null);
        return dto;
    }

    public static DtoAdmin toDto(Admin admin) {
        DtoAdmin dto = new DtoAdmin();
        dto.setId(admin.getId());
        dto.setAdminName(admin.getAdminName());
        // StoreId kaldırıldı, çünkü admin entity'de yok
        return dto;
    }

    public static DtoStore toDto(Store store) {
        DtoStore dto = new DtoStore();
        dto.setId(store.getId());
        dto.setStoreName(store.getStoreName());
        dto.setChairCount(store.getChairCount());
        dto.setAdminId(store.getAdmin() != null ? store.getAdmin().getId() : null);
        return dto;
    }

    public static ReservationResponse toDto(Reservation reservation) {

        ReservationResponse dto = new ReservationResponse();

        dto.setId(reservation.getId());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setReservationDate(reservation.getReservationDate());

        dto.setChairName(reservation.getChair().getChairName());
        dto.setChairId(reservation.getChair().getId());

        dto.setEmployeeName(
                reservation.getChair() != null && reservation.getChair().getEmployee() != null
                        ? reservation.getChair().getEmployee().getEmployeeName()
                        : null
        );

        dto.setStoreId(reservation.getStore().getId());
        dto.setStoreName(reservation.getStore().getStoreName());

        // 🔥 TELEFON KARARI TEK NOKTADA
        if (reservation.getUser() != null) {
            dto.setUserName(reservation.getUser().getUserName());
            dto.setPhoneNumber(reservation.getUser().getPhoneNumber());
            dto.setGuest(false);
        } else {
            dto.setUserName(reservation.getCustomerName() + " " + reservation.getCustomerSurname());
            dto.setPhoneNumber(reservation.getCustomerPhone());
            dto.setGuest(true);
        }

        return dto;
    }

}
