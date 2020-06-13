package com.ab.pension.withdrawal;

import com.ab.pension.model.Account;
import com.ab.pension.model.AccountClosingStatus;
import com.ab.pension.service.BackgroundCheckService;
import com.ab.pension.setup.BackgroundCheckResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Arpit Bhardwaj
 */

@ExtendWith(MockitoExtension.class)
class AccountClosingServiceTest {

    public static final String FIRST_NAME = "Prakash";
    public static final String LAST_NAME = "Kumar";
    public static final String TAX_ID = "8678jgg7";

    @Mock
    private BackgroundCheckService backgroundCheckService;

    private AccountClosingService underTest;
    Instant fixedTime = LocalDate.of(1955,6,13).atStartOfDay(ZoneId.systemDefault()).toInstant();
    Clock clock = Clock.fixed(fixedTime, ZoneId.systemDefault());

    @BeforeEach
    void setUp() {
        underTest = new AccountClosingService(backgroundCheckService,clock);
    }

    @Test
    public void shouldDeclineAccountClosingTodayIfHolderReachesRetirementTomorrow() throws IOException {
        Account account = new Account();
        account.setDob(LocalDate.of(1955, 6,14));
        AccountClosingResponse accountClosingResponse = underTest.closeAccount(account);
        assertEquals(AccountClosingStatus.DENIED, accountClosingResponse.getStatus());
        System.out.println(accountClosingResponse.getProcessingDate());
        assertEquals(LocalDateTime.ofInstant(fixedTime,ZoneOffset.systemDefault()),accountClosingResponse.getProcessingDate());
    }

    @Test
    public void shouldApproveAccountClosingIfHolderReachesRetirementAgeToday() throws IOException {
        Instant fixedTime = LocalDate.of(2019, 7, 4)
                .atStartOfDay(ZoneId.systemDefault()).toInstant();
        Clock clock = Clock.fixed(fixedTime, ZoneId.systemDefault());
        AccountClosingService underTest = new AccountClosingService(backgroundCheckService, clock);

        Account account = new Account();
        account.setFirstName(FIRST_NAME);
        account.setLastName(LAST_NAME);
        account.setTaxId(TAX_ID);
        final LocalDate dob = LocalDate.of(1954, 7, 4);
        account.setDob(dob);

        BDDMockito.given(backgroundCheckService.confirm(FIRST_NAME, LAST_NAME, TAX_ID, dob))
                .willReturn(new BackgroundCheckResults("OK", 1));

        final AccountClosingResponse accountClosingResponse = underTest.closeAccount(account);
        assertEquals(AccountClosingStatus.CLOSED, accountClosingResponse.getStatus());
        assertEquals(LocalDateTime.ofInstant(fixedTime, ZoneOffset.systemDefault()), accountClosingResponse.getProcessingDate());
    }
}