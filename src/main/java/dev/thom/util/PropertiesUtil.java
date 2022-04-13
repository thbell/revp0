package dev.thom.util;

import java.util.Properties;

public class PropertiesUtil {

    public static Properties getDatabaseTestProperties() {

        Properties databaseProperties = new Properties();
        databaseProperties.setProperty("url", "jdbc:postgresql://revaturedb.cilcmbbm7xyz.us-east-1.rds.amazonaws.com/revaturedb");
        databaseProperties.setProperty("user", "postgres");
        databaseProperties.setProperty("password", "");

        return databaseProperties;
    }
}
