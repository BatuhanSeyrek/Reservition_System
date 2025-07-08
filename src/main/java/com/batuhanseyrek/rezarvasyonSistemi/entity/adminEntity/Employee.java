package com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = "chair")
@Table(name = "employee")
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "employeeName")
    private String employeeName;
    @ManyToOne
    @JoinColumn(name = "admin_id") // FK chair tablosuna yazılır
    @JsonIgnore
    private Admin admin;
    @JoinColumn(name = "chair_id")
    @OneToOne
    private Chair chair;
}
