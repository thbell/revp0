package dev.thom.dao;

import dev.thom.model.Account;
import dev.thom.util.ThomList;

public interface AccountDao {

    // create
    Account createAccount(Account account);

    // read
    ThomList<Account> getAccountListByBankUserId(Integer bankUserId);
    Account getAccountByAccountId(Integer accountId);

    // update
    Account withdrawFunds(Integer accountId, Double amount);
    Account depositFunds(Integer accountId, Double amount);

    // delete
    void deleteAccountByAccountId(Integer accountId);


}
