package com.chauhan.hospitalManagementSystem.entity;

import com.chauhan.hospitalManagementSystem.enums.BloodGroupType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@ToString
@Getter
@Setter
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate birthDate;

    private String email;

    private String gender;

    @Enumerated(value = EnumType.STRING)
    private BloodGroupType bloodGroup;

    @CreationTimestamp
    private LocalDateTime createdAt;

//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_insurance", unique = true)
    private Insurance insurance; // owning side
//
//    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // inverse side
//    @ToString.Exclude
    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL)//inverse side [whenever we delete the patient then all the appointments will be deleted of this patient]
    private Set<Appointment> appointments = new HashSet<>();

}