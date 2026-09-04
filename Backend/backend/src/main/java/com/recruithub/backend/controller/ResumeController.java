package com.recruithub.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.recruithub.backend.entity.Resume;
import com.recruithub.backend.service.ResumeService;

@RestController
@RequestMapping("/resumes")
@CrossOrigin(origins = "*")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("studentId") Integer studentId) {

        try {

            if (file.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Please select a resume");
            }

            if (!file.getOriginalFilename()
                    .toLowerCase()
                    .endsWith(".pdf")) {

                return ResponseEntity
                        .badRequest()
                        .body("Only PDF files are allowed");
            }

            Resume resume =
                    resumeService.uploadResume(
                            file,
                            studentId);

            return ResponseEntity.ok(resume);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}