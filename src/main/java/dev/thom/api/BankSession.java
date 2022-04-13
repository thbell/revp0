package dev.thom.api;

import dev.thom.dao.AccountDaoImpl;
import dev.thom.dao.UserDaoImpl;
import dev.thom.model.Account;
import dev.thom.model.BankUser;
import dev.thom.model.type.AccountType;
import dev.thom.model.type.TransactionType;
import dev.thom.service.AccountService;
import dev.thom.service.AccountServiceImpl;
import dev.thom.service.UserService;
import dev.thom.service.UserServiceImpl;
import dev.thom.util.LogLevel;
import dev.thom.util.PasswordUtil;
import dev.thom.util.ThomList;
import dev.thom.util.ThomLogger;

import java.io.Console;
import java.util.Properties;
import java.util.Scanner;

public class BankSession {

    private UserService userService;
    private AccountService accountService;

    private BankUser bankUser;
    private Account selectedAccount;

    Scanner scanner;

    public BankSession(Properties databaseProperties) {

        userService = new UserServiceImpl(new UserDaoImpl(databaseProperties));
        accountService = new AccountServiceImpl(new AccountDaoImpl(databaseProperties));

        this.scanner = new Scanner(System.in);

        ThomLogger.log("Bank initialized", LogLevel.INFO);
    }

    public static String makeBannerString(String input) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("****** ")
                .append(input)
                .append(" ******");

        return stringBuilder.toString();
    }

    public static String makeVisibleLineBreak(int multiplier, String string) {
        String repeated = new String(new char[multiplier]).replace("\0", string);
        return repeated;
    }

    public void resetUserSession() {
        bankUser = null;
        selectedAccount = null;
    }

    public void mainMenu() {

        resetUserSession();

        System.out.println();
        System.out.println(makeBannerString("Welcome to Thom Bank!"));
        System.out.println("Main Menu");
        System.out.println("[1] Create User Account");
        System.out.println("[2] Log in to Existing Account");
        System.out.println("[3] Exit");
        System.out.println("[4] Password Hash Util");
        System.out.println("Choose [1-4]:");
        String choice = scanner.next();

        switch (choice) {
            case "1":
                showCreateUserMenu();
//                showInputHashSubMenu();
                break;

            case "2":
                showLoginMenu();

            case "3":
                System.exit(0);

            case "4":
                showInputHashSubMenu();

            default:
                mainMenu();

        }

    }

    public void showCreateUserMenu() {
        String firstName = "";
        String lastName = "";
        String username = "";
        String password = "";

        System.out.println();
        System.out.println(makeBannerString("Create New User Account"));

        System.out.println("Enter First Name:");
        firstName = scanner.next();

        System.out.println("Enter Last Name:");
        lastName = scanner.next();

        System.out.println("Enter Username:");
        username = scanner.next();

        System.out.println("Enter Password:");

        Console console = System.console();
        if (console != null) {
            char[] passwordBytes = console.readPassword();
            password = String.valueOf(passwordBytes);
        } else {
            password = scanner.next();
        }

        String hashedPassword = PasswordUtil.hashString(password);

        BankUser newBankUser = new BankUser();
        newBankUser.setFirstName(firstName);
        newBankUser.setLastName(lastName);
        newBankUser.setUserName(username);
        newBankUser.setPasswordHash(hashedPassword);

        if (userService != null) {

            bankUser = userService.createBankUser(newBankUser);

            if (bankUser != null) {
                System.out.println("New Bank User: " + newBankUser.getUserName());

                showCreateNewAccountMenu();

            } else {

                mainMenu();

            }

        } else {

            mainMenu();

        }


    }

    public void showCreateNewAccountMenu() {

        System.out.println();
        System.out.println(makeBannerString("Create New Account"));

        System.out.println("Select Account Type");
        System.out.println("[1] Checking");
        System.out.println("[2] Savings");
        System.out.println("Choose [1-2]:");

        String accountTypeChoice = scanner.next();

        AccountType accountType = null;

        switch (accountTypeChoice) {
            case "1":
                accountType = AccountType.CHECKING;
                break;

            case "2":
                accountType = AccountType.SAVINGS;
                break;

            default:
                showCreateNewAccountMenu();

        }

        System.out.println("Enter starting deposit:");
        Double initialDeposit = scanner.nextDouble();

        Account newAccount = new Account();
        newAccount.setAccountType(accountType);
        newAccount.setBankUserId(bankUser.getId());
        newAccount.setBalance(initialDeposit);

        selectedAccount = accountService.createAccount(newAccount);

        showBankUserMenu();

    }

    public void showLoginMenu() {

        String username = "";
        String password = "";

        System.out.println();
        System.out.println(makeBannerString("Log in to Existing Account"));
        System.out.println("Enter User Name:");
        username = scanner.next();

        System.out.println("Enter Password:");

        Console console = System.console();
        if (console != null) {
            char[] passwordBytes = console.readPassword();
            password = String.valueOf(passwordBytes);
        } else {
            password = scanner.next();
        }


        System.out.println("Login Details:");
        System.out.printf("User Name: %s\n", username);
        System.out.printf("Password: %s\n", PasswordUtil.hideString("*", password.length()));

        String hasedPassword = PasswordUtil.hashString(password);

        if (userService != null) {
            bankUser = userService.getBankUserByCredentials(username, hasedPassword);

            if (bankUser != null && PasswordUtil.hasValue(bankUser.getUserName())) {

                System.out.printf("Logged in as user: %s\n", bankUser.getUserName());

                ThomList<Account> accountList = accountService.getAccountListByBankUserId(bankUser.getId());
                selectedAccount = accountList.get(0);

            } else {

                System.out.println();
                System.out.println("> Invalid login.");
                mainMenu();

            }

        }



        showBankUserMenu();


    }

//    public void showDepositMenu() {
//        System.out.println();
//
//        System.out.println(makeBannerString("Make Deposit"));
//
//        if (selectedAccount != null) {
//            System.out.printf("Account ID: %s\n", selectedAccount.getId());
//            System.out.printf("Account Balance: $%.2f\n", selectedAccount.getBalance());
//            System.out.println();
//
//            Double depositAmount = 0.00;
//            System.out.println("Deposit Amount:");
//            depositAmount = scanner.nextDouble();
//
//            Account updatedAccount = accountService.depositFunds(selectedAccount.getId(), depositAmount);
//
//            if (updatedAccount.getErrorMessageList() != null) {
//
//                ThomList<String> errorMessageList = updatedAccount.getErrorMessageList();
//
//                for (int i = 0; i < errorMessageList.size(); i++ ) {
//                    System.out.printf("> %s", errorMessageList.get(i));
//                }
//                showWithdrawalMenu();
//
//            } else {
//                selectedAccount = updatedAccount;
//            }
//
//            System.out.println();
//        }
//
//        showBankUserMenu();
//    }

//    public void showWithdrawalMenu() {
//        System.out.println();
//
//        System.out.println(makeBannerString("Withdraw Funds"));
//
//        if (selectedAccount != null) {
//            System.out.printf("Account ID: %s\n", selectedAccount.getId());
//            System.out.printf("Account Balance: $%.2f\n", selectedAccount.getBalance());
//            System.out.println();
//
//            Double withdrawalAmount = 0.00;
//            System.out.println("Withdrawal Amount:");
//            withdrawalAmount = scanner.nextDouble();
//
//
//
//            if (updatedAccount.getErrorMessageList() != null) {
//
//                ThomList<String> errorMessageList = updatedAccount.getErrorMessageList();
//
//                for (int i = 0; i < errorMessageList.size(); i++ ) {
//                    System.out.println(errorMessageList.get(i));
//                }
//                showWithdrawalMenu();
//
//            } else {
//                selectedAccount = updatedAccount;
//            }
//            System.out.println();
//        }
//
//        showBankUserMenu();
//    }
    public void showModifyAccountBalanceMenu(TransactionType transactionType) {

        System.out.println();

        System.out.println(makeBannerString("Make Deposit"));

        if (selectedAccount != null) {
            System.out.printf("Account ID: %s\n", selectedAccount.getId());
            System.out.printf("Account Balance: $%.2f\n", selectedAccount.getBalance());
            System.out.println();

            Double transactionAmount = 0.00;
            System.out.printf("%s Amount: ", transactionType);
            transactionAmount = scanner.nextDouble();

            Account updatedAccount = new Account();

            if (TransactionType.DEPOSIT.equals(transactionType)) {

                updatedAccount = accountService.depositFunds(selectedAccount.getId(), transactionAmount);

            } else if (TransactionType.WITHDRAWAL.equals(transactionType)) {

                updatedAccount = accountService.withdrawFunds(selectedAccount.getId(), transactionAmount);

            }


            if (updatedAccount.getErrorMessageList() != null) {

                ThomList<String> errorMessageList = updatedAccount.getErrorMessageList();

                for (int i = 0; i < errorMessageList.size(); i++ ) {
                    System.out.printf("> %s", errorMessageList.get(i));
                }
                showModifyAccountBalanceMenu(transactionType);

            } else {
                selectedAccount = updatedAccount;
            }

            System.out.println();
        }

        showBankUserMenu();

    }

    public void showSelectAccountMenu() {
        System.out.println();

        ThomList<Account> accountList = accountService.getAccountListByBankUserId(bankUser.getId());

        System.out.println(makeBannerString("Manage Account"));
        System.out.println("Select Account");

        for (int i = 0; i < accountList.size(); i++) {
            int accountIndex = i + 1;
            Account account = accountList.get(i);
            System.out.printf("[%s] %s  $%.2f %s\n", i + 1, account.getId(), account.getBalance(), account.getAccountType().toString());
            System.out.println(makeVisibleLineBreak(30, "-"));
        }

        System.out.printf("Choose [1-%d]:\n", accountList.size());
        Integer choice = scanner.nextInt();

        selectedAccount = accountList.get(choice - 1);

        showBankUserMenu();
    }

    public void showTransactionHistoryMenu() {
        System.out.println();

//        ThomList<Account> accountList = accountService.getAccountListByBankUserId(bankUser.getId());

//        System.out.println(makeBannerString("Transaction History"));
//        System.out.printf("Account ID: %s\n", selectedAccount.getId());
//        System.out.printf("Account Balance: $%.2f\n", selectedAccount.getBalance());

//        for (int i = 0; i < accountList.size(); i++) {
//            int accountIndex = i + 1;
//            Account account = accountList.get(i);
//            System.out.printf("[%s] %s  $%.2f\n", i + 1, account.getId(), account.getBalance());
//            System.out.println(makeVisibleLineBreak(20, "-"));
//        }
//
//        Integer choice = scanner.nextInt();
//
//        selectedAccount = accountList.get(choice - 1);

        showBankUserMenu();
    }

    public void showBankUserMenu() {

        System.out.println();
        System.out.println(makeBannerString("Manage Account"));

        if (selectedAccount != null) {
            System.out.printf("Account ID: %s\n", selectedAccount.getId());
            System.out.printf("Account Balance: $%.2f\n", selectedAccount.getBalance());
            System.out.println();
            System.out.println("Menu");
            System.out.println("[1] Select Account");
            System.out.println("[2] Make Deposit");
            System.out.println("[3] Make Withdrawal");
            System.out.println("[4] Transaction History");
            System.out.println("[5] Create New Account");
            System.out.println("[6] Close Selected Account");
            System.out.println("[7] Close User Account");
            System.out.println("[8] Logout");
            System.out.println("Choose [1-6]:");

            String choice = scanner.next();

            switch (choice) {
                case "1":
                    showSelectAccountMenu();
                    break;

                case "2":
//                    showDepositMenu();
                    showModifyAccountBalanceMenu(TransactionType.DEPOSIT);
                    break;

                case "3":
//                    showWithdrawalMenu();
                    showModifyAccountBalanceMenu(TransactionType.WITHDRAWAL);
                    break;

                case "4":
                    showTransactionHistoryMenu();
                    break;

                case "5":
                    showCreateNewAccountMenu();
                    break;

                case "6":
                    accountService.deleteAccountByAccountId(selectedAccount.getId());
                    ThomList<Account> currentAccountList = accountService.getAccountListByBankUserId(bankUser.getId());

                    if (!currentAccountList.isEmpty()) {
                        selectedAccount = currentAccountList.get(0);
                        showBankUserMenu();
                    } else {
                        System.out.println("No more accounts");
                        showCreateNewAccountMenu();
                    }

                    break;

                case "7":
                    userService.deleteBankUserById(bankUser.getId());
                    mainMenu();
                    break;

                case "8":
                    mainMenu();
                    break;

                default:
                    mainMenu();
            }
        } else {
            mainMenu();
        }

    }

    public void showInputHashSubMenu() {

        boolean finished = false;

        while(!finished) {

            System.out.println("Input string to hash:");
            String input = scanner.nextLine();

            switch (input) {
                case "exit":
                    finished = true;
                    break;

                default:
                    String hashedInput = PasswordUtil.hashString(input);
                    System.out.printf("Hashed: %s\n", hashedInput);
                    break;
            }

        }

    }

}
