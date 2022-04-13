package dev.thom.dao;

import dev.thom.model.BankUser;
import dev.thom.model.DatabaseCredentials;
import dev.thom.model.type.UserRoleType;
import dev.thom.util.ConnectionUtil;
import dev.thom.util.LogLevel;
import dev.thom.util.ThomLogger;

import java.sql.*;
import java.util.Properties;

public class UserDaoImpl extends Dao implements UserDao {

    public static String CREATE_BANK_USER = "" +
            " INSERT INTO bank_user ( \n" +
            "   user_role_type_id, \n" +
            "   first_name, \n" +
            "   last_name, \n" +
            "   username, \n" +
            "   password_hash, \n" +
            "   create_date, \n" +
            "   last_login_date \n" +
            ") \n" +
            " VALUES ( \n" +
            "   (SELECT user_role_type_id FROM user_role_type WHERE user_role_type_name = ?), \n" +
            "   ?, \n" +
            "   ?, \n" +
            "   ?, \n" +
            "   ?, \n" +
            "   current_date, \n" +
            "   current_date \n" +
            " )\n";

    public static String GET_BANK_USER_BY_CREDENTIALS = "" +
            " SELECT \n" +
            "   bank_user_id, \n" +
            "   urt.user_role_type_name, \n" +
            "   first_name, \n" +
            "   last_name, \n" +
            "   username, \n" +
            "   password_hash, \n" +
            "   create_date, \n" +
            "   last_login_date \n" +
            " FROM bank_user bu \n" +
            " INNER JOIN user_role_type urt on bu.user_role_type_id = urt.user_role_type_id \n" +
            " WHERE username = ? \n" +
            " AND password_hash = ? \n";

    public static String GET_BANK_USER_BY_ID = "" +
            " SELECT \n" +
            "   bank_user_id, \n" +
            "   urt.user_role_type_name, \n" +
            "   first_name, \n" +
            "   last_name, \n" +
            "   username, \n" +
            "   password_hash, \n" +
            "   create_date, \n" +
            "   last_login_date \n" +
            " FROM bank_user bu \n" +
            " INNER JOIN user_role_type urt on bu.user_role_type_id = urt.user_role_type_id \n" +
            " WHERE bank_user_id = ? \n";

    public static String DELETE_ACCOUNTS_BY_BANK_USER_ID = "" +
            "DELETE FROM account \n" +
            "USING bank_user \n" +
            "WHERE account.bank_user_id = bank_user .bank_user_id \n" +
            "AND account.bank_user_id = ? \n";

    public static String DELETE_BANK_USER_BY_BANK_USER_ID = "" +
            "DELETE FROM bank_user \n" +
            "WHERE bank_user_id = ? \n";


    public UserDaoImpl(Properties databaseProperties) {
        super(databaseProperties);
    }


    @Override
    public BankUser createBankUser(BankUser bankUser) {
        BankUser newBankUser = new BankUser();


        try (Connection connection = this.getConnectionProxy().getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_BANK_USER,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, UserRoleType.USER.toString());
            preparedStatement.setString(2, bankUser.getFirstName());
            preparedStatement.setString(3, bankUser.getLastName());
            preparedStatement.setString(4, bankUser.getUserName());
            preparedStatement.setString(5, bankUser.getPasswordHash());

            preparedStatement.execute();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                newBankUser = getBankUserById(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            ThomLogger.log(e.getMessage(), LogLevel.ERROR);
        }


        return newBankUser;
    }

    @Override
    public BankUser getBankUserByCredentials(String userName, String passwordHash) {
        BankUser bankUser = new BankUser();

        try (Connection connection = this.getConnectionProxy().getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(GET_BANK_USER_BY_CREDENTIALS);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, passwordHash);

            ResultSet resultSet = preparedStatement.executeQuery();

            bankUser = getBankUserFromResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
            ThomLogger.log(e.getMessage(), LogLevel.ERROR);
        }


        return bankUser;
    }

    @Override
    public BankUser getBankUserById(Integer bankUserId) {
        BankUser bankUser = new BankUser();

        try (Connection connection = this.getConnectionProxy().getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(GET_BANK_USER_BY_ID);
            preparedStatement.setInt(1, bankUserId);

            ResultSet resultSet = preparedStatement.executeQuery();

            bankUser = getBankUserFromResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
            ThomLogger.log(e.getMessage(), LogLevel.ERROR);
        }


        return bankUser;
    }

    @Override
    public void deleteBankUserById(Integer bankUserId) {

        try (Connection connection = this.getConnectionProxy().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ACCOUNTS_BY_BANK_USER_ID,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, bankUserId);
            preparedStatement.executeUpdate();

            PreparedStatement preparedStatement2 = connection.prepareStatement(DELETE_BANK_USER_BY_BANK_USER_ID,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement2.setDouble(1, bankUserId);
            preparedStatement2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            ThomLogger.log(e.getMessage(), LogLevel.ERROR);
        }



    }

    private BankUser getBankUserFromResultSet(ResultSet resultSet) throws SQLException {
        BankUser bankUser = new BankUser();

        if (resultSet.next()) {

            Integer id = resultSet.getInt("bank_user_id");
            String userRoleTypeName = resultSet.getString("user_role_type_name");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String username = resultSet.getString("username");
            String passwordHash = resultSet.getString("password_hash");
            Long createDate = resultSet.getTimestamp("create_date").getTime();
            Long lastLoginDate = resultSet.getTimestamp("last_login_date").getTime();

            bankUser.setId(id);

            switch (userRoleTypeName) {
                case "USER":
                    bankUser.setUserRoleType(UserRoleType.USER);
                    break;

                case "ADMIN":
                    bankUser.setUserRoleType(UserRoleType.ADMIN);
                    break;

                default:
                    break;
            }
            bankUser.setFirstName(firstName);
            bankUser.setLastName(lastName);
            bankUser.setUserName(username);
            bankUser.setPasswordHash(passwordHash);
            bankUser.setCreateDate(createDate);
            bankUser.setLastLoginDate(lastLoginDate);

        }

        return bankUser;
    }

}
