package com.recruithub.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recruithub.backend.entity.Application;
import com.recruithub.backend.entity.Company;
import com.recruithub.backend.entity.Student;
import com.recruithub.backend.repository.ApplicationRepository;
import com.recruithub.backend.repository.CompanyRepository;
import com.recruithub.backend.repository.StudentRepository;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ZohoCRMService zohoCRMService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;


    // APPLY FOR COMPANY
    public Application apply(Application application) {

        // Check whether student already applied
        boolean alreadyApplied =
                applicationRepository.existsByStudentIdAndCompanyId(
                        application.getStudentId(),
                        application.getCompanyId()
                );

        if (alreadyApplied) {

            throw new RuntimeException(
                    "Student has already applied to this company"
            );
        }

        // Set initial application status
        application.setStatus("Applied");

        // Save application in MySQL
        Application savedApplication =
                applicationRepository.save(application);

        // Find student
        Optional<Student> studentOptional =
                studentRepository.findById(
                        application.getStudentId()
                );

        // Find company
        Optional<Company> companyOptional =
                companyRepository.findById(
                        application.getCompanyId()
                );

        // Send candidate to Zoho CRM
        if (studentOptional.isPresent()
                && companyOptional.isPresent()) {

            Student student =
                    studentOptional.get();

            Company company =
                    companyOptional.get();

            try {

                String zohoResponse =
                        zohoCRMService.createLead(
                                student.getName(),
                                student.getEmail(),
                                company.getCompanyName(),
                                company.getRoleName(),
                                company.getRequiredSkills(),
                                0
                        );

                System.out.println(
                        "Zoho CRM Response: "
                        + zohoResponse
                );

            } catch (Exception e) {

                System.out.println(
                        "Zoho CRM integration failed: "
                        + e.getMessage()
                );
            }
        }

        return savedApplication;
    }


    
    // GET APPLICATIONS OF A STUDENT
    public List<Application> getApplicationsByStudent(int studentId) {
        return applicationRepository.findByStudentId(studentId);
    }


    // UPDATE APPLICATION STATUS
    public Application updateApplicationStatus(
            int applicationId,
            String status) {

        Application application =
                applicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"
                                ));

        application.setStatus(status);

        return applicationRepository.save(application);
    }


    // WITHDRAW APPLICATION
    public void withdrawApplication(
            int applicationId) {

        applicationRepository.deleteById(
                applicationId
        );
    }
}