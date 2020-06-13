package com.ab.pension.withdrawal;

import com.ab.pension.model.Account;
import com.ab.pension.model.AccountClosingStatus;
import com.ab.pension.service.BackgroundCheckService;
import com.ab.pension.setup.AccountOpeningService;
import com.ab.pension.setup.BackgroundCheckResults;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * @author Arpit Bhardwaj
 */
public class AccountClosingService {
    private static final String UNACCEPTABLE_RISK_PROFILE = "HIGH";
    public static final int RETIREMENT_AGE = 65;
    private BackgroundCheckService backgroundCheckService;
    private Clock clock;

    public AccountClosingService(BackgroundCheckService backgroundCheckService,Clock clock) {
        this.backgroundCheckService = backgroundCheckService;
        this.clock = clock;
    }

    public AccountClosingResponse closeAccount(Account account) throws IOException {
        Period accountHolderAge = Period.between(account.getDob(), LocalDate.now(clock));
        if (accountHolderAge.getYears() < RETIREMENT_AGE){
            return new AccountClosingResponse(AccountClosingStatus.DENIED, LocalDateTime.now(clock));
        }else {
            BackgroundCheckResults backgroundCheckResults = backgroundCheckService.confirm(
                    account.getFirstName(),
                    account.getLastName(),
                    account.getTaxId(),
                    account.getDob());

            if (backgroundCheckResults == null){
                return new AccountClosingResponse(AccountClosingStatus.PENDING, LocalDateTime.now(clock));
            }else {
                String riskProfile = backgroundCheckResults.getRiskProfile();
                if (riskProfile.equals(UNACCEPTABLE_RISK_PROFILE)){
                    return new AccountClosingResponse(AccountClosingStatus.PENDING, LocalDateTime.now(clock));
                }else {
                    return new AccountClosingResponse(AccountClosingStatus.CLOSED, LocalDateTime.now(clock));
                }
            }
        }
    }
}
