package com.batuhanseyrek.rezarvasyonSistemi.dto.response;

import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoChair;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoEmployee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoAdminFull {
    private DtoAdmin admin;
    private List<DtoChair> chairs;
    private List<DtoEmployee> employees;
}
