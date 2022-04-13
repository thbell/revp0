package dev.thom.api;

import dev.thom.model.DatabaseCredentials;

import java.util.Properties;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Properties databaseProperties = new Properties();
        databaseProperties.setProperty("url", "jdbc:postgresql://revaturedb.cilcmbbm7xyz.us-east-1.rds.amazonaws.com/revaturedb");
        databaseProperties.setProperty("user", "postgres");
        databaseProperties.setProperty("password", args[0]);

        BankSession bankSession = new BankSession(databaseProperties);
        bankSession.mainMenu();

    }



}
