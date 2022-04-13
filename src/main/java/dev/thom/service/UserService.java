package dev.thom.service;

import dev.thom.model.BankUser;

public interface UserService {

    // create
    BankUser createBankUser(BankUser bankUser);

    // read
    BankUser getBankUserByCredentials(String userName, String passwordHash);
    BankUser getBankUserById(Integer bankUserId);

    // delete
    void deleteBankUserById(Integer bankUserId);

}
