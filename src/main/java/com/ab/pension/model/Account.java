package com.ab.pension.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Set;

/**
 * @author Arpit Bhardwaj
 */
public class Account {

    private String id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String taxId;
    private BigDecimal totalInvestmentValue;
    private Currency currency;
    private Set<String> investments;
    private BigDecimal availableCash;
    private LocalDateTime expectedRetirement;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public BigDecimal getTotalInvestmentValue() {
        return totalInvestmentValue;
    }

    public void setTotalInvestmentValue(BigDecimal totalInvestmentValue) {
        this.totalInvestmentValue = totalInvestmentValue;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Set<String> getInvestments() {
        return investments;
    }

    public void setInvestments(Set<String> investments) {
        this.investments = investments;
    }

    public BigDecimal getAvailableCash() {
        return availableCash;
    }

    public void setAvailableCash(BigDecimal availableCash) {
        this.availableCash = availableCash;
    }

    public LocalDateTime getExpectedRetirement() {
        return expectedRetirement;
    }

    public void setExpectedRetirement(LocalDateTime expectedRetirement) {
        this.expectedRetirement = expectedRetirement;
    }
}
