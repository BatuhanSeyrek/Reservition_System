package com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalTime;

@Entity
@Table(name = "chair")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"employee", "admin", "store"})
public class Chair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "openingTime")
    private LocalTime openingTime;

    @Column(name = "closingTime")
    private LocalTime closingTime;

    @Column(name = "islemSuresi")
    private LocalTime islemSuresi;

    @Column(name = "chairName")
    private String chairName;

    @OneToOne(mappedBy = "chair")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @PrePersist
    public void setDefaultIslemSuresi() {
        if (islemSuresi == null) {
            this.islemSuresi = new Time(1 * 60 * 60 * 1000).toLocalTime(); // 1 saat
        }
    }
}
