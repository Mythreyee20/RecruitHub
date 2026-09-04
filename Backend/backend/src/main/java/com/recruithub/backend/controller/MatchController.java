package com.recruithub.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.recruithub.backend.service.MatchService;

@RestController
@RequestMapping("/matches")
@CrossOrigin(origins = "*")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/student/{studentId}")
    public List<String> getMatches(
            @PathVariable Integer studentId) {

        return matchService.getJobMatches(studentId);
    }
}