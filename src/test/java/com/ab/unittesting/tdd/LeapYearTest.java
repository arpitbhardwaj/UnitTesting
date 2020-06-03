package com.ab.unittesting.tdd;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Arpit Bhardwaj
 */
public class LeapYearTest {

    @Test
    public void leapYearsAndDivisibleByFour() {
        assertTrue(LeapYear.isLeapYear(2016));
    }
}