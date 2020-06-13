package com.ab.pension.setup;

import com.ab.pension.model.AccountOpeningStatus;
import com.ab.pension.repository.AccountRepository;
import com.ab.pension.service.BackgroundCheckService;

import java.time.LocalDate;

/**
 * @author Arpit Bhardwaj
 */
public class AccountOpeningService {

    private static final String UNACCEPTABLE_RISK_PROFILE = "HIGH";
    private BackgroundCheckService backgroundCheckService;
    private ReferenceIdsManager referenceIdsManager;
    private AccountRepository accountRepository;

    public AccountOpeningService(BackgroundCheckService backgroundCheckService, ReferenceIdsManager referenceIdsManager, AccountRepository accountRepository) {
        this.backgroundCheckService = backgroundCheckService;
        this.referenceIdsManager = referenceIdsManager;
        this.accountRepository = accountRepository;
    }

    public AccountOpeningStatus openAccount(String firstName, String lastName, String taxId, LocalDate dob){
        BackgroundCheckResults backgroundCheckResults = backgroundCheckService.confirm(firstName,lastName,taxId,dob);

        if (backgroundCheckResults == null || backgroundCheckResults.getRiskProfile().equals(UNACCEPTABLE_RISK_PROFILE)){
            return AccountOpeningStatus.DECLINED;
        }else{
            String id = referenceIdsManager.obtainId(firstName,lastName,taxId,dob);
            if (id != null){
                accountRepository.save(id,firstName,lastName,taxId,dob,backgroundCheckResults);
                return AccountOpeningStatus.OPENED;
            }else {
                return AccountOpeningStatus.DECLINED;
            }
        }
    }
}
