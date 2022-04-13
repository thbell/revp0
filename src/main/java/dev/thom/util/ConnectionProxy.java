package dev.thom.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionProxy {

    private Properties databaseProperties;

    public ConnectionProxy(Properties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        connection = ConnectionUtil.createConnection(this.databaseProperties);
        return connection;
    }
}
