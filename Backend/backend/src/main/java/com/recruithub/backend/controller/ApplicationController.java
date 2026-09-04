package com.recruithub.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recruithub.backend.entity.Application;
import com.recruithub.backend.service.ApplicationService;

@RestController
@RequestMapping("/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public Application apply(@RequestBody Application application) {
        return applicationService.apply(application);
    }

    @GetMapping("/student/{studentId}")
    public List<Application> getApplications(
            @PathVariable int studentId) {

        return applicationService
                .getApplicationsByStudent(studentId);
    }

    @PutMapping("/{applicationId}/status/{status}")
    public Application updateStatus(
            @PathVariable int applicationId,
            @PathVariable String status) {

        return applicationService
                .updateApplicationStatus(applicationId, status);
    }

    @DeleteMapping("/{applicationId}")
    public String withdrawApplication(
            @PathVariable int applicationId) {

        applicationService.withdrawApplication(applicationId);

        return "Application withdrawn successfully";
    }
}