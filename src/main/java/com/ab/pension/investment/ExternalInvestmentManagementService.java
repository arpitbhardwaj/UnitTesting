package com.ab.pension.investment;

import com.ab.pension.model.Account;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * @author Arpit Bhardwaj
 */
public class ExternalInvestmentManagementService implements InvestmentManagementService {

    public static final MathContext MATH_CONTEXT = new MathContext(34, RoundingMode.DOWN);

    public ExternalInvestmentManagementService() {
    }

    @Override
    public void addFunds(Account account, BigDecimal investmentAmount, Currency investmentCurrency) {
        account.setAvailableCash(account.getAvailableCash().add(investmentAmount, MATH_CONTEXT));
    }

    @Override
    public boolean buyInvestmentFund(Account account, String fundId, BigDecimal investmentAmount) throws IOException {

        if (account.getAvailableCash().compareTo(investmentAmount) < 0){
            throw new IllegalArgumentException("Not Enough Available Cash to invest");
        }
        if (executeInvestmentTransaction(fundId,investmentAmount,"BUY")){
            account.setAvailableCash(account.getAvailableCash().subtract(investmentAmount,MATH_CONTEXT));
            account.getInvestments().add(fundId);
            return true;
        }else{
            return false;
        }
    }

    //new operator over here makes it difficult to test as its created internal to this class and cant be supplied from outside
    public boolean executeInvestmentTransaction(String fundId, BigDecimal investmentAmount, String direction) {
        return new ExternalBrokerLink().executeInvestmentTransaction(fundId, investmentAmount, direction);
    }

    @Override
    public boolean sellInvestmentFund(Account account, String fundId, BigDecimal investmentAmount) throws IOException {
        if (!account.getInvestments().contains(fundId)){
            throw new IllegalArgumentException("Account does't have any holdings in " + fundId);
        }
        if (executeInvestmentTransaction(fundId,investmentAmount,"SELL")){
            account.setAvailableCash(account.getAvailableCash().add(investmentAmount,MATH_CONTEXT));
            account.getInvestments().remove(fundId);
            return true;
        }else{
            return false;
        }
    }
}
