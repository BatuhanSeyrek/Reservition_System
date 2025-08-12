package com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoRegisterAdmin {
    private Long id;
    private String adminName;
    private String password;
    private String storeName;
    private int chairCount;
}
