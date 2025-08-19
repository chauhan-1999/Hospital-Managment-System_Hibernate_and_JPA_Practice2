package com.chauhan.hospitalManagementSystem.controller;

import com.chauhan.hospitalManagementSystem.dto.AppointmentResponseDto;
import com.chauhan.hospitalManagementSystem.dto.CreateAppointmentRequestDto;
import com.chauhan.hospitalManagementSystem.dto.PatientResponseDto;
import com.chauhan.hospitalManagementSystem.service.AppointmentService;
import com.chauhan.hospitalManagementSystem.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponseDto> createNewAppointment(@RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.createNewAppointment(createAppointmentRequestDto));
    }

    @GetMapping("/profile")
    private ResponseEntity<PatientResponseDto> getPatientProfile() {
        Long patientId = 4L;
        return ResponseEntity.ok(patientService.getPatientById(patientId));
    }

}
