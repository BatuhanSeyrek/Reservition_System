package com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "admin")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"chairs", "employees", "store"})
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="adminName", nullable = false)
    private String adminName;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Store store;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chair> chairs;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees;
}
