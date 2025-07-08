package com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "admin")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"chairs", "employees"}) // 🔥 Kritik satır bu
public class Admin {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="adminName", nullable = false)
    private String adminName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "chairCount")
    private Integer chairCount;

    @Column(name = "storeName")
    private String storeName;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chair> chairs;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees;
}