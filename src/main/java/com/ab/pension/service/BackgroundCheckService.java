package com.ab.pension.service;

import com.ab.pension.setup.BackgroundCheckResults;

import java.time.LocalDate;

/**
 * @author Arpit Bhardwaj
 */
public interface BackgroundCheckService {
    BackgroundCheckResults confirm(String firstName, String lastName, String taxId, LocalDate dob);
}
