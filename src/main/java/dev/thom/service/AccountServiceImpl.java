package dev.thom.service;

import dev.thom.dao.AccountDao;
import dev.thom.model.Account;
import dev.thom.util.ThomList;

public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account createAccount(Account account) {
        return this.accountDao.createAccount(account);
    }

    @Override
    public ThomList<Account> getAccountListByBankUserId(Integer bankUserId) {
        return this.accountDao.getAccountListByBankUserId(bankUserId);
    }

    @Override
    public Account getAccountByAccountId(Integer accountId) {
        return this.accountDao.getAccountByAccountId(accountId);
    }

    @Override
    public Account withdrawFunds(Integer accountId, Double amount) {
        return this.accountDao.withdrawFunds(accountId, amount);
    }

    @Override
    public Account depositFunds(Integer accountId, Double amount) {
        return this.accountDao.depositFunds(accountId, amount);
    }

    @Override
    public void deleteAccountByAccountId(Integer accountId) {
        this.accountDao.deleteAccountByAccountId(accountId);
    }

}
