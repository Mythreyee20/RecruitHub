package com.recruithub.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recruithub.backend.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findByEmail(String email);

}