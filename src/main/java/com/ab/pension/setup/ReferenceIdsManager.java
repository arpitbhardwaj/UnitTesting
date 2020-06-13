package com.ab.pension.setup;

import java.time.LocalDate;

/**
 * @author Arpit Bhardwaj
 */
public interface ReferenceIdsManager {
    String obtainId(String firstName, String lastName, String taxId, LocalDate dob);
}
