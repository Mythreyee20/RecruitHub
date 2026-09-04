package com.recruithub.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recruithub.backend.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findById(Integer id);
}