package dev.thom.dao;

import dev.thom.model.Account;
import dev.thom.model.BankUser;
import dev.thom.model.type.AccountType;
import dev.thom.util.PropertiesUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountDaoImplTest {


    static AccountDao accountDao = new AccountDaoImpl(PropertiesUtil.getDatabaseTestProperties());
    static UserDao userDao = new UserDaoImpl(PropertiesUtil.getDatabaseTestProperties());

//    Account createAccount(Account account);
//
//    // read
//    ThomList<Account> getAccountListByBankUserId(Integer bankUserId);
//    Account getAccountByAccountId(Integer accountId);
//
//    // update
//    Account withdrawFunds(Integer accountId, Double amount);
//    Account depositFunds(Integer accountId, Double amount);
//
//    // delete
//    void deleteAccountByAccountId(Integer accountId);

    static String USER_NAME = "blahblah";
    Integer accountId;

    @Test
    void createAccountTest() {


        BankUser bankUser = new BankUser();
        bankUser.setFirstName("Test");
        bankUser.setLastName("TestLast");
        bankUser.setUserName(USER_NAME);
        bankUser.setPasswordHash("blahblah123");

        BankUser newBankUser = userDao.createBankUser(bankUser);


        Account account = new Account();
        account.setAccountType(AccountType.CHECKING);
        account.setBankUserId(newBankUser.getId());
        account.setBalance(200.50);

        Account newAccount = accountDao.createAccount(account);

        this.accountId = newAccount.getId();
        Assertions.assertTrue(newAccount != null && newAccount.getId() > 0);
    }

    private void cleanUp() {


    }

}
