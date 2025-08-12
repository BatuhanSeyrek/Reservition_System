package com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity;

import com.batuhanseyrek.rezarvasyonSistemi.entity.userEntity.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "store")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"admin", "chairs", "reservations"})
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "chair_count")
    private Integer chairCount;

    @OneToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private Admin admin;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chair> chairs;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;
}