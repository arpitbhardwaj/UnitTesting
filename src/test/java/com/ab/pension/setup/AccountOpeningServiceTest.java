package com.ab.pension.setup;

import com.ab.pension.model.AccountOpeningStatus;
import com.ab.pension.repository.AccountRepository;
import com.ab.pension.service.BackgroundCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Arpit Bhardwaj
 */
class AccountOpeningServiceTest {

    public static final String FIRST_NAME = "Prakash";
    public static final String LAST_NAME = "Kumar";
    public static final String TAX_ID = "8678jgg7";
    public static final LocalDate DOB = LocalDate.of(1983, 1, 1);

    private static final String UNACCEPTABLE_RISK_PROFILE = "HIGH";
    private static final String ACCEPTABLE_RISK_PROFILE = "LOW";
    public static final String REFERENCE_ID = "reference_id";
    private AccountOpeningService underTest;
    private BackgroundCheckService backgroundCheckService = mock(BackgroundCheckService.class);
    private ReferenceIdsManager referenceIdsManager = mock(ReferenceIdsManager.class);
    private AccountRepository accountRepository = mock(AccountRepository.class);

    @BeforeEach
    void setUp() {
        underTest = new AccountOpeningService(backgroundCheckService,referenceIdsManager,accountRepository);
    }

    @Test
    void shouldOpenAccount() {
        //setup
        when(backgroundCheckService.confirm(FIRST_NAME,LAST_NAME,TAX_ID,DOB))
                .thenReturn(new BackgroundCheckResults(ACCEPTABLE_RISK_PROFILE,0));

        when(referenceIdsManager.obtainId(FIRST_NAME,LAST_NAME,TAX_ID,DOB))
                .thenReturn(REFERENCE_ID);
        //execution
        AccountOpeningStatus accountOpeningStatus = underTest.openAccount(FIRST_NAME, LAST_NAME, TAX_ID, DOB);

        //verification
        assertEquals(AccountOpeningStatus.OPENED,accountOpeningStatus);
    }

    @Test
    void shouldDeclineAccountIfUnacceptableRiskProfileBackgroundCheckResponseReceived() {
        //setup
        when(backgroundCheckService.confirm(FIRST_NAME,LAST_NAME,TAX_ID,DOB))
                .thenReturn(new BackgroundCheckResults(UNACCEPTABLE_RISK_PROFILE,0));
        //execution
        AccountOpeningStatus accountOpeningStatus = underTest.openAccount(FIRST_NAME, LAST_NAME, TAX_ID, DOB);

        //verification
        assertEquals(AccountOpeningStatus.DECLINED,accountOpeningStatus);
    }
    @Test
    void shouldDeclineAccountIfNullBackgroundCheckResponseReceived() {
        //setup
        when(backgroundCheckService.confirm(FIRST_NAME,LAST_NAME,TAX_ID,DOB))
                .thenReturn(null);
        //execution
        AccountOpeningStatus accountOpeningStatus = underTest.openAccount(FIRST_NAME, LAST_NAME, TAX_ID, DOB);

        //verification
        assertEquals(AccountOpeningStatus.DECLINED,accountOpeningStatus);
    }
}