package com.ab.pension.investment;

import com.ab.pension.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Arpit Bhardwaj
 */

@ExtendWith(MockitoExtension.class)
class ExternalInvestmentManagementServiceTest {

    public static final String FUND_ID = "fund_id";

    @Spy
    private ExternalInvestmentManagementService underTest;

    @Test
    public void shouldBeAbleToButPensionFundInvestmentIfEnoughCashInAccount() throws IOException {
        when(underTest.executeInvestmentTransaction(anyString(),any(BigDecimal.class),anyString()))
                .thenReturn(true);

        Account account = new Account();
        account.setInvestments(new HashSet<>());
        BigDecimal startingAccountBalance = new BigDecimal(1000000);
        account.setAvailableCash(startingAccountBalance);
        BigDecimal desiredInvestmentAmount = new BigDecimal(100000);
        underTest.buyInvestmentFund(account, FUND_ID, desiredInvestmentAmount);

        assertEquals(account.getAvailableCash(), startingAccountBalance.subtract(desiredInvestmentAmount));
        assertTrue(account.getInvestments().contains(FUND_ID));
    }
}