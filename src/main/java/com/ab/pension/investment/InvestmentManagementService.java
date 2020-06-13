package com.ab.pension.investment;

import com.ab.pension.model.Account;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author Arpit Bhardwaj
 */
public interface InvestmentManagementService {

    void addFunds(Account account, BigDecimal investmentAmount, Currency investmentCurrency);
    boolean buyInvestmentFund(Account account, String fundId, BigDecimal investmentAmount) throws IOException;
    boolean sellInvestmentFund(Account account, String fundId, BigDecimal investmentAmount) throws IOException;
}
