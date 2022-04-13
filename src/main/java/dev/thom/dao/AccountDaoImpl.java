package dev.thom.dao;

import dev.thom.model.Account;
import dev.thom.model.type.AccountType;
import dev.thom.util.*;

import java.sql.*;
import java.util.Properties;

public class AccountDaoImpl extends Dao implements AccountDao {

    public static String CREATE_ACCOUNT = "" +
            " INSERT INTO account ( \n" +
            "   account_type_id, \n" +
            "   bank_user_id, \n" +
            "   balance, \n" +
            "   create_date \n" +
            " ) \n" +
            " VALUES ( \n" +
            "   (SELECT account_type_id FROM account_type WHERE account_type_name = ?), \n" +
            "   (select bank_user_id FROM bank_user WHERE bank_user_id = ?), \n" +
            "   ?, \n" +
            "   current_date \n" +
            " ) \n";

    public static String INSERT = "insert into account (last_name, first_name) value(?, ?)";

    public static String GET_ACCOUNT_LIST_BY_BANK_USER_ID = "" +
            " SELECT \n" +
            "   account_id, \n" +
            "   act.account_type_name, \n" +
            "   bank_user_id, \n" +
            "   balance, \n" +
            "   create_date \n" +
            " FROM account a \n" +
            " INNER JOIN account_type act ON a.account_type_id = act.account_type_id \n" +
            " WHERE bank_user_id = ? \n";

    public static String GET_ACCOUNT_BY_ACCOUNT_ID = "" +
            " SELECT \n" +
            "   account_id, \n" +
            "   act.account_type_name, \n" +
            "   bank_user_id, \n" +
            "   balance, \n" +
            "   create_date \n" +
            " FROM account a \n" +
            " INNER JOIN account_type act ON a.account_type_id = act.account_type_id \n" +
            " WHERE account_id = ? \n";

    public static String UPDATE_ACCOUNT_BALANCE = "" +
            " UPDATE account \n" +
            "   SET balance = ? \n" +
            "   WHERE account_id = ? \n";

    public static String DELETE_ACCOUNT_BY_ID = "" +
            "DELETE FROM account \n" +
            "WHERE account_id = ? \n";


    public AccountDaoImpl(Properties databaseProperties) {
        super(databaseProperties);
    }

    @Override
    public Account createAccount(Account account) {
        Account newAccount = new Account();

        try (Connection connection = this.getConnectionProxy().getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ACCOUNT,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getAccountType().toString());
            preparedStatement.setInt(2, account.getBankUserId());
            preparedStatement.setDouble(3, account.getBalance());

            preparedStatement.execute();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                Integer accountId = generatedKeys.getInt("account_id");
                newAccount = getAccountByAccountId(accountId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return newAccount;
    }

    @Override
    public ThomList<Account> getAccountListByBankUserId(Integer bankUserId) {

        ThomList<Account> accountList = new ArrayList<>();

        try (Connection connection = this.getConnectionProxy().getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_LIST_BY_BANK_USER_ID);
            preparedStatement.setInt(1, bankUserId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                accountList.add(getAccountFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountList;
    }

    @Override
    public Account getAccountByAccountId(Integer accountId) {

        Account account = new Account();

        try (Connection connection = this.getConnectionProxy().getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_BY_ACCOUNT_ID);
            preparedStatement.setInt(1, accountId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                account = getAccountFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return account;
    }

    @Override
    public Account withdrawFunds(Integer accountId, Double amount) {

        Account currentAccount = getAccountByAccountId(accountId);
        boolean hasEnoughMoney = Double.compare(currentAccount.getBalance(), amount) > 0 && amount > 0;

        if (hasEnoughMoney) {
            Double newBalance = currentAccount.getBalance() - amount;
            currentAccount = updateAccountBalance(accountId, newBalance);
        } else {

            ThomList<String> errorMessageList = new ArrayList<>();

            if (amount < 0) {
                errorMessageList.add("Amount must be more than 0.");
            } else {
                errorMessageList.add("Insufficient funds.");
            }

            currentAccount = new Account();
            currentAccount.setId(accountId);
            currentAccount.setErrorMessageList(errorMessageList);

        }

        return currentAccount;
    }

    @Override
    public Account depositFunds(Integer accountId, Double amount) {

        Account currentAccount = getAccountByAccountId(accountId);

        if (amount > 0) {
            currentAccount = getAccountByAccountId(accountId);

            Double newBalance = currentAccount.getBalance() + amount;
            currentAccount = updateAccountBalance(accountId, newBalance);
        } else {

            ThomList<String> errorMessageList = new ArrayList<>();
            errorMessageList.add("Amount must be more than 0.");
            currentAccount.setErrorMessageList(errorMessageList);

        }


        return currentAccount;
    }

    @Override
    public void deleteAccountByAccountId(Integer accountId) {
        try (Connection connection = this.getConnectionProxy().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ACCOUNT_BY_ID,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, accountId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            ThomLogger.log(e.getMessage(), LogLevel.ERROR);
        }


    }

    private Account updateAccountBalance(Integer accountId, Double newBalance) {
        Account account = new Account();

        try (Connection connection = this.getConnectionProxy().getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT_BALANCE,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setInt(2, accountId);


            preparedStatement.execute();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                account = getAccountByAccountId(generatedKeys.getInt("account_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            ThomLogger.log(e.getMessage(), LogLevel.ERROR);
        }

        return account;
    }

    private Account getAccountFromResultSet(ResultSet resultSet) throws SQLException {

        Account account = new Account();

        Integer accountId = resultSet.getInt("account_id");
        String accountTypeName = resultSet.getString("account_type_name");
        Integer bankUserId = resultSet.getInt("bank_user_id");
        Double balance = resultSet.getDouble("balance");
        Long createDate = resultSet.getTimestamp("create_date").getTime();

        account.setId(accountId);
        account.setBankUserId(bankUserId);
        account.setBalance(balance);
        account.setCreateDate(createDate);

        switch (accountTypeName) {
            case "CHECKING":
                account.setAccountType(AccountType.CHECKING);
                break;

            case "SAVINGS":
                account.setAccountType(AccountType.SAVINGS);
                break;

            default:
                break;
        }

        return account;
    }
}
