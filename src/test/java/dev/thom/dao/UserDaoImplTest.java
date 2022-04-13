package dev.thom.dao;

import dev.thom.model.BankUser;
import dev.thom.util.PropertiesUtil;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoImplTest {

    static AccountDao accountDao = new AccountDaoImpl(PropertiesUtil.getDatabaseTestProperties());
    static UserDao userDao = new UserDaoImpl(PropertiesUtil.getDatabaseTestProperties());

//    // create
//    BankUser createBankUser(BankUser bankUser);
//
//    // read
//    BankUser getBankUserByCredentials(String userName, String passwordHash);
//    BankUser getBankUserById(Integer bankUserId);
//
//    // delete
//    void deleteBankUserById(Integer bankUserId);

    static String USER_NAME = "blahblah";
    static String USER_PASS = "testing";

    @Test
    @Order(1)
    void createUserTest() {
        BankUser bankUser = new BankUser();
        bankUser.setFirstName("Test");
        bankUser.setLastName("TestLast");
        bankUser.setUserName(USER_NAME);
        bankUser.setPasswordHash(USER_PASS);

        BankUser newBankUser = userDao.createBankUser(bankUser);

        Assertions.assertTrue(newBankUser != null && newBankUser.getId() > 0);

    }

    @Test
    @Order(2)
    void getBankUserByCredentials() {

        BankUser bankUser = userDao.getBankUserByCredentials(USER_NAME, USER_PASS);
        Assertions.assertTrue(bankUser != null && bankUser.getId() > 0);

    }
}
