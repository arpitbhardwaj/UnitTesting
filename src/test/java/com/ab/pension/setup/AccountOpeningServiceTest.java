package com.ab.pension.setup;

import com.ab.pension.model.AccountOpeningStatus;
import com.ab.pension.repository.AccountRepository;
import com.ab.pension.service.BackgroundCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
    public static final String ACCOUNT_ID = "account_id";
    private AccountOpeningService underTest;
    private BackgroundCheckService backgroundCheckService = mock(BackgroundCheckService.class);
    private ReferenceIdsManager referenceIdsManager = mock(ReferenceIdsManager.class);
    private AccountRepository accountRepository = mock(AccountRepository.class);
    private AccountOpeningEventPublisher eventPublisher = mock(AccountOpeningEventPublisher.class);

    @BeforeEach
    void setUp() {
        underTest = new AccountOpeningService(backgroundCheckService,referenceIdsManager,accountRepository,eventPublisher);
    }

    @Test
    void shouldOpenAccount() throws IOException {
        //setup
        BackgroundCheckResults backgroundCheckResults = new BackgroundCheckResults(ACCEPTABLE_RISK_PROFILE, 0);
        when(backgroundCheckService.confirm(FIRST_NAME,LAST_NAME,TAX_ID,DOB))
                .thenReturn(backgroundCheckResults);

        /*//org.mockito.exceptions.misusing.InvalidUseOfMatchersException:
        //This exception may occur if matchers are combined with raw values:
        //When using matchers, all arguments have to be provided by matchers.
        when(referenceIdsManager.obtainId(FIRST_NAME,anyString(),LAST_NAME,TAX_ID,DOB))
                .thenReturn(REFERENCE_ID);*/

        when(referenceIdsManager.obtainId(
                eq(FIRST_NAME),
                anyString(),
                eq(LAST_NAME),
                eq(TAX_ID),
                eq(DOB)))
                .thenReturn(ACCOUNT_ID);

        //execution
        AccountOpeningStatus accountOpeningStatus = underTest.openAccount(FIRST_NAME, LAST_NAME, TAX_ID, DOB);

        //verification
        assertEquals(AccountOpeningStatus.OPENED,accountOpeningStatus);

        //Use Argument Capture to capture values of argument passed
        ArgumentCaptor<BackgroundCheckResults> backgroundCheckResultsArgumentCaptor = ArgumentCaptor.forClass(BackgroundCheckResults.class);

        verify(eventPublisher).notify(ACCOUNT_ID);

        /*//org.mockito.exceptions.misusing.InvalidUseOfMatchersException:
        //This exception may occur if captors are combined with raw values:
        //When using matchers/captors, all arguments have to be provided by matchers.
        verify(accountRepository).save(ACCOUNT_ID,FIRST_NAME,LAST_NAME,TAX_ID,DOB,backgroundCheckResultsArgumentCaptor.capture());*/
        verify(accountRepository).save(
                eq(ACCOUNT_ID),
                eq(FIRST_NAME),
                eq(LAST_NAME),
                eq(TAX_ID),
                eq(DOB),
                backgroundCheckResultsArgumentCaptor.capture());

        /*//verify stubbed method is redundant and should not be done
        //Stubbing gives you implicit verification
        //verification means you dont care about the return values so you don't stub
        verify(backgroundCheckService).confirm(FIRST_NAME,LAST_NAME,TAX_ID,DOB);*/

        //System.out.println(backgroundCheckResultsArgumentCaptor.getValue().getRiskProfile() + " : " + backgroundCheckResultsArgumentCaptor.getValue().getUpperAccountLimit());
        assertEquals(backgroundCheckResults.getRiskProfile(), backgroundCheckResultsArgumentCaptor.getValue().getRiskProfile());
        assertEquals(backgroundCheckResults.getUpperAccountLimit(), backgroundCheckResultsArgumentCaptor.getValue().getUpperAccountLimit());

        //to ensure no more unverified invocation on your mock
        //stub invocation will be by default counted unverified and need to be ignored manually using ignoreStub
        verifyNoMoreInteractions(ignoreStubs(backgroundCheckService));
        verifyNoMoreInteractions(accountRepository,eventPublisher);
    }
    @Test
    void shouldThrowIfBackgroundServiceThrows() throws IOException {
        //setup
        when(backgroundCheckService.confirm(FIRST_NAME,LAST_NAME,TAX_ID,DOB))
                .thenThrow(new IOException());

        //execution and verification
        assertThrows(IOException.class,() -> underTest.openAccount(FIRST_NAME, LAST_NAME, TAX_ID, DOB));
    }

    @Test
    void shouldThrowIfReferenceIdManagerThrows() throws IOException {
        //setup
        when(backgroundCheckService.confirm(FIRST_NAME,LAST_NAME,TAX_ID,DOB))
                .thenReturn(new BackgroundCheckResults(ACCEPTABLE_RISK_PROFILE,0));

        when(referenceIdsManager.obtainId(
                eq(FIRST_NAME),
                anyString(),
                eq(LAST_NAME),
                eq(TAX_ID),
                eq(DOB)))
                .thenThrow(new RuntimeException());

        //execution and verification
        assertThrows(RuntimeException.class,() -> underTest.openAccount(FIRST_NAME, LAST_NAME, TAX_ID, DOB));
    }

    @Test
    void shouldThrowIfAccountRepositoryThrows() throws IOException {
        //setup
        BackgroundCheckResults backgroundCheckResults = new BackgroundCheckResults(ACCEPTABLE_RISK_PROFILE, 0);
        when(backgroundCheckService.confirm(FIRST_NAME,LAST_NAME,TAX_ID,DOB))
                .thenReturn(backgroundCheckResults);

        when(referenceIdsManager.obtainId(
                eq(FIRST_NAME),
                anyString(),
                eq(LAST_NAME),
                eq(TAX_ID),
                eq(DOB)))
                .thenReturn(ACCOUNT_ID);

        when(accountRepository.save(ACCOUNT_ID,FIRST_NAME,LAST_NAME,TAX_ID,DOB,backgroundCheckResults))
                .thenThrow(new RuntimeException());
        //execution and verification
        assertThrows(RuntimeException.class,() -> underTest.openAccount(FIRST_NAME, LAST_NAME, TAX_ID, DOB));
    }

    @Test
    void shouldThrowIfEventPublisherThrows() throws IOException {
        //setup
        BackgroundCheckResults backgroundCheckResults = new BackgroundCheckResults(ACCEPTABLE_RISK_PROFILE, 0);
        when(backgroundCheckService.confirm(FIRST_NAME,LAST_NAME,TAX_ID,DOB))
                .thenReturn(backgroundCheckResults);

        when(referenceIdsManager.obtainId(
                eq(FIRST_NAME),
                anyString(),
                eq(LAST_NAME),
                eq(TAX_ID),
                eq(DOB)))
                .thenReturn(ACCOUNT_ID);

        when(accountRepository.save(ACCOUNT_ID,FIRST_NAME,LAST_NAME,TAX_ID,DOB,backgroundCheckResults))
                .thenReturn(true);
        //compile error cannot keep void method call inside when clause
        /*when(eventPublisher.notify(ACCOUNT_ID))
                .thenThrow(new RuntimeException());*/

        doThrow(new RuntimeException()).when(eventPublisher).notify(ACCOUNT_ID);
        //execution and verification
        assertThrows(RuntimeException.class,() -> underTest.openAccount(FIRST_NAME, LAST_NAME, TAX_ID, DOB));
    }

    @Test
    void shouldDeclineAccountIfUnacceptableRiskProfileBackgroundCheckResponseReceived() throws IOException {
        //setup
        when(backgroundCheckService.confirm(FIRST_NAME,LAST_NAME,TAX_ID,DOB))
                .thenReturn(new BackgroundCheckResults(UNACCEPTABLE_RISK_PROFILE,0));
        //execution
        AccountOpeningStatus accountOpeningStatus = underTest.openAccount(FIRST_NAME, LAST_NAME, TAX_ID, DOB);

        //verification
        assertEquals(AccountOpeningStatus.DECLINED,accountOpeningStatus);
    }
    @Test
    void shouldDeclineAccountIfNullBackgroundCheckResponseReceived() throws IOException {
        //setup
        when(backgroundCheckService.confirm(FIRST_NAME,LAST_NAME,TAX_ID,DOB))
                .thenReturn(null);
        //execution
        AccountOpeningStatus accountOpeningStatus = underTest.openAccount(FIRST_NAME, LAST_NAME, TAX_ID, DOB);

        //verification
        assertEquals(AccountOpeningStatus.DECLINED,accountOpeningStatus);
    }
}