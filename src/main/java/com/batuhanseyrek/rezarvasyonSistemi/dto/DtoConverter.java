package com.batuhanseyrek.rezarvasyonSistemi.dto;

import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoChair;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoEmployee;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.ReservationResponse;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Employee;
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
        return dto;
    }

    public static DtoAdmin toDto(Admin admin) {
        DtoAdmin dto = new DtoAdmin();
        dto.setId(admin.getId());
        dto.setAdminName(admin.getAdminName());
        dto.setStoreName(admin.getStoreName());
        dto.setChairCount(admin.getChairCount());

        return dto;
    }
    public static ReservationResponse toDto(Reservation reservation) {
        ReservationResponse dto = new ReservationResponse();

        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setChairName(reservation.getChair().getChairName());
        dto.setEmployeeName(reservation.getChair().getEmployee().getEmployeeName());
        dto.setUserName(reservation.getUser().getUserName());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setEmployeeName(reservation.getChair().getEmployee().getEmployeeName());
        return dto;
    }
}
