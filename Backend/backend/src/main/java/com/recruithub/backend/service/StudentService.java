package com.recruithub.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recruithub.backend.entity.Student;
import com.recruithub.backend.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student registerStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student login(String email, String password) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            throw new RuntimeException("Email not found");
        }
        if (!student.getPassword().equals(password)) {
            throw new RuntimeException("Incorrect password");
        }
        return student;
    }
}