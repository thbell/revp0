package dev.thom.util;

import dev.thom.model.DatabaseCredentials;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {

    public static Connection createConnection(Properties databaseProperties) throws SQLException {

//        StringBuilder connectionBuilder = new StringBuilder();


//        if (databaseCredentials != null) {
//            connectionBuilder.append("jdbc:postgresql://")
//                    .append(databaseCredentials.getUrl())
//                    .append("/").append(databaseCredentials.getDatabaseId())
//                    .append("?user=").append(databaseCredentials.getUsername())
//                    .append("&password=").append(databaseCredentials.getPassword());
//        }


//        String connectionString = String.format(
//                "jdbc:postgresql://%s/%s?user=%s&password=%s",
//                databaseCredentials.getUrl(),
//                databaseCredentials.getDatabaseId(),
//                databaseCredentials.getUsername(),
//                databaseCredentials.getPassword()
//        );

//        System.out.println("DB Password: " + databaseProperties.getProperty("password"));
        String url = databaseProperties.getProperty("url","");
        Connection connection = DriverManager.getConnection(url, databaseProperties);


        return connection;
    }
}
