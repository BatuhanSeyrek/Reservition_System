package com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Time;

@Entity
@Table(name = "chair")
@ToString(exclude = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chair {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "openingTime")
    private Time openingTime;
    @Column(name = "closingTime")
    private Time closingTime;
    @Column(name="islemSuresi")
    private Time islemSuresi;
    @Column(name = "chairName")
    private String chairName;
    @OneToOne(mappedBy = "chair")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "admin_id") // FK chair tablosuna yazılır
    @JsonIgnore
    private Admin admin;
    @PrePersist
    public void setDefaultIslemSuresi() {
        if (islemSuresi == null) {
            this.islemSuresi = new Time(1 * 60 * 60 * 1000); // 1 saat = 3600000 ms
        }
    }

}
