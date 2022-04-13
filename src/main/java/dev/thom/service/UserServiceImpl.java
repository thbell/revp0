package dev.thom.service;

import dev.thom.dao.UserDao;
import dev.thom.model.BankUser;


public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public BankUser createBankUser(BankUser bankUser) {
        return this.userDao.createBankUser(bankUser);
    }

    @Override
    public BankUser getBankUserByCredentials(String userName, String passwordHash) {
        return this.userDao.getBankUserByCredentials(userName, passwordHash);
    }

    @Override
    public BankUser getBankUserById(Integer bankUserId) {
        return this.userDao.getBankUserById(bankUserId);
    }

    @Override
    public void deleteBankUserById(Integer bankUserId) {
        this.userDao.deleteBankUserById(bankUserId);
    }
}
