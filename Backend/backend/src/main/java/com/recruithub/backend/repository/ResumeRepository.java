package com.recruithub.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recruithub.backend.entity.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Integer> {

    Resume findTopByStudentIdOrderByIdDesc(Integer studentId);
}