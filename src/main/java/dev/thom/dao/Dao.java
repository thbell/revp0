package dev.thom.dao;

import dev.thom.util.ConnectionProxy;

import java.util.Properties;

public class Dao {

    private ConnectionProxy connectionProxy;

    public Dao(Properties databaseProperties) {
//        this.databaseProperties = databaseProperties;
        this.connectionProxy = new ConnectionProxy(databaseProperties);
    }

    public ConnectionProxy getConnectionProxy() {
        return connectionProxy;
    }
}
