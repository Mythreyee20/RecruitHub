package com.recruithub.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recruithub.backend.entity.Company;
import com.recruithub.backend.entity.Resume;
import com.recruithub.backend.repository.CompanyRepository;
import com.recruithub.backend.repository.ResumeRepository;

@Service
public class MatchService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public List<String> getJobMatches(Integer studentId) {

        Resume resume =
                resumeRepository.findTopByStudentIdOrderByIdDesc(studentId);

        List<String> results = new ArrayList<>();

        if (resume == null || resume.getSkills() == null) {
            return results;
        }

        String[] studentSkills =
                resume.getSkills().toLowerCase().split(",");

        List<Company> companies =
                companyRepository.findAll();

        for (Company company : companies) {

            if (company.getRequiredSkills() == null) {
                continue;
            }

            String[] requiredSkills =
                    company.getRequiredSkills()
                            .toLowerCase()
                            .split(",");

            int matchedSkills = 0;

            List<String> missingSkills = new ArrayList<>();

            for (String required : requiredSkills) {

                required = required.trim();

                boolean skillMatched = false;

                for (String student : studentSkills) {

                    if (student.trim().equals(required)) {
                        matchedSkills++;
                        skillMatched = true;
                        break;
                    }
                }

                if (!skillMatched) {
                    missingSkills.add(required);
                }
            }

            int totalSkills = requiredSkills.length;

            int matchPercentage =
                    (matchedSkills * 100) / totalSkills;

            String missingSkillsText;

            if (missingSkills.isEmpty()) {
                missingSkillsText = "None";
            } else {
                missingSkillsText =
                        String.join(", ", missingSkills);
            }

            results.add(
                    company.getCompanyName()
                    + " - "
                    + company.getRoleName()
                    + " : "
                    + matchPercentage
                    + "% | Missing Skills: "
                    + missingSkillsText
            );
        }

        return results;
    }
}