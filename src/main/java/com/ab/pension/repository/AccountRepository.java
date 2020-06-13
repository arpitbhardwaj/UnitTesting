package com.ab.pension.repository;

import com.ab.pension.model.Account;
import com.ab.pension.setup.BackgroundCheckResults;

import java.time.LocalDate;

/**
 * @author Arpit Bhardwaj
 */
public interface AccountRepository {
    boolean save(String id, String firstName, String lastName, String taxId, LocalDate dob, BackgroundCheckResults backgroundCheckResults);
    boolean isExpired(Account account);
}
