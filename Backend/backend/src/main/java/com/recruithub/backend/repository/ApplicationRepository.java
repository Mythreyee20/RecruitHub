package com.recruithub.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recruithub.backend.entity.Application;

public interface ApplicationRepository
        extends JpaRepository<Application, Integer> {

    List<Application> findByStudentId(int studentId);

    boolean existsByStudentIdAndCompanyId(int studentId, int companyId);
}