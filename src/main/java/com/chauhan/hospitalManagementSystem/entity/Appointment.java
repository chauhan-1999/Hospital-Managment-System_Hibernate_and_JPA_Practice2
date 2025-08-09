package com.chauhan.hospitalManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString //not good practice
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @Column(length = 500)
    private String reason;

    @ManyToOne // owning side
    @JoinColumn(nullable = false)
    @ToString.Exclude //either you do this or set fetchType.Lazy [to avoid stackOverFlow error]
    @JsonIgnore //this filed will not be added as json inside another dto
    private Patient patient;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(nullable = false)
    @ManyToOne
    @JoinColumn(nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Doctor doctor;//owning side
}
