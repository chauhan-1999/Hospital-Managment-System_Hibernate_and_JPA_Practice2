package com.chauhan.hospitalManagementSystem;


import com.chauhan.hospitalManagementSystem.entity.Patient;
import com.chauhan.hospitalManagementSystem.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PatientTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void testPatient() {
//        List<Patient> patientList = patientRepository.findAll();
//        List<CPatientInfo> patientList = patientRepository.getAllPatientsInfoConcrete();
//        List<BloodGroupStats> patientList = patientRepository.getBloodGroupStats();
//
//        for(var p: patientList) {
//            System.out.println(p);
//        }

//        int rowsAffected = patientRepository.updatePatientNameWithId("Anuj Sharma", 1L);
//
//        System.out.println(rowsAffected);

//        List<Patient> patientList = patientRepository.findAll(); //This will give N+1 query problem
         /*
        Hibernate: select p1_0.id,p1_0.birth_date,p1_0.blood_group,p1_0.created_at,p1_0.email,p1_0.gender,p1_0.patient_insurance,p1_0.name from patient p1_0 [one hibernet query to get all the  [5]patients]
        [but we have 5 more queries to get the appointment's of their patient] -> this is called N+1 problem in which if u want to do one query then u r doing N queries more where N is the result of 1st query
        //if want to get the patient their appointments in one query and u don't want to see different queries [N queries]
        //N+1 problem Solution -> You have to write ur own custom query
Hibernate: select a1_0.patient_id,a1_0.id,a1_0.appointment_time,a1_0.doctor_id,d1_0.id,d1_0.email,d1_0.name,d1_0.specialization,a1_0.reason from appointment a1_0 left join doctor d1_0 on d1_0.id=a1_0.doctor_id where a1_0.patient_id=?
Hibernate: select a1_0.patient_id,a1_0.id,a1_0.appointment_time,a1_0.doctor_id,d1_0.id,d1_0.email,d1_0.name,d1_0.specialization,a1_0.reason from appointment a1_0 left join doctor d1_0 on d1_0.id=a1_0.doctor_id where a1_0.patient_id=?
Hibernate: select a1_0.patient_id,a1_0.id,a1_0.appointment_time,a1_0.doctor_id,d1_0.id,d1_0.email,d1_0.name,d1_0.specialization,a1_0.reason from appointment a1_0 left join doctor d1_0 on d1_0.id=a1_0.doctor_id where a1_0.patient_id=?
Hibernate: select a1_0.patient_id,a1_0.id,a1_0.appointment_time,a1_0.doctor_id,d1_0.id,d1_0.email,d1_0.name,d1_0.specialization,a1_0.reason from appointment a1_0 left join doctor d1_0 on d1_0.id=a1_0.doctor_id where a1_0.patient_id=?
Hibernate: select a1_0.patient_id,a1_0.id,a1_0.appointment_time,a1_0.doctor_id,d1_0.id,d1_0.email,d1_0.name,d1_0.specialization,a1_0.reason from appointment a1_0 left join doctor d1_0 on d1_0.id=a1_0.doctor_id where a1_0.patient_id=?
Patient(id=1, name=Aarav Sharma, birthDate=1990-05-10, email=aarav.sharma@example.com, gender=MALE, bloodGroup=O_POSITIVE, createdAt=null, insurance=null, appointments=[])
Patient(id=2, name=Diya Patel, birthDate=1995-08-20, email=diya.patel@example.com, gender=FEMALE, bloodGroup=A_POSITIVE, createdAt=null, insurance=null, appointments=[])
Patient(id=3, name=Dishant Verma, birthDate=1988-03-15, email=dishant.verma@example.com, gender=MALE, bloodGroup=A_POSITIVE, createdAt=null, insurance=null, appointments=[])
Patient(id=4, name=Neha Iyer, birthDate=1992-12-01, email=neha.iyer@example.com, gender=FEMALE, bloodGroup=AB_POSITIVE, createdAt=null, insurance=null, appointments=[])
Patient(id=5, name=Kabir Singh, birthDate=1993-07-11, email=kabir.singh@example.com, gender=MALE, bloodGroup=O_POSITIVE, createdAt=null, insurance=null, appointments=[])
 */
        List<Patient> patientList = patientRepository.getAllPatientsWithAppointments(); //to optimize N+1 query problem we have to our own custom query

        for(var p: patientList) {
            System.out.println(p);
        }
    }
}

