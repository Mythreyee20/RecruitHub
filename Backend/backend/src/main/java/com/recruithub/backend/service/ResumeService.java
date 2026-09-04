package com.recruithub.backend.service;

import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.recruithub.backend.entity.Resume;
import com.recruithub.backend.repository.ResumeRepository;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    // EXTRACT SKILLS FROM RESUME
    private String extractSkills(String text) {

        String lowerText = text.toLowerCase();

        StringBuilder skills = new StringBuilder();

        String[] skillList = {
            "java",
            "python",
            "javascript",
            "html",
            "css",
            "react",
            "spring boot",
            "sql",
            "mysql",
            "mongodb",
            "dsa",
            "oop",
            "dbms",
            "aws",
            "docker",
            "git",
            "github",
            "machine learning",
            "artificial intelligence"
        };

        for (String skill : skillList) {

            if (lowerText.contains(skill)) {

                if (skills.length() > 0) {
                    skills.append(", ");
                }

                skills.append(skill);
            }
        }

        return skills.toString();
    }

    // UPLOAD RESUME
    public Resume uploadResume(
            MultipartFile file,
            Integer studentId) throws IOException {

        PDDocument document =
                Loader.loadPDF(file.getBytes());

        PDFTextStripper stripper =
                new PDFTextStripper();

        String text =
                stripper.getText(document);

        String skills =
                extractSkills(text);

        document.close();

        Resume resume = new Resume();

        resume.setStudentId(studentId);
        resume.setFileName(file.getOriginalFilename());
        resume.setResumeText(text);
        resume.setSkills(skills);

        return resumeRepository.save(resume);
    }
}