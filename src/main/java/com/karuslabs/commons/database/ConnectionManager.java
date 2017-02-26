/*
 * Copyright (C) 2016 KarusMC @ karusmc.com
 * All rights reserved.
 */
package com.karuslabs.commons.database;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import org.bukkit.configuration.file.FileConfiguration;

public class ConnectionManager {

    private volatile static ConnectionManager uniqueInstance;
    private MongoClient client;
    private FileConfiguration config;

    private ConnectionManager(FileConfiguration config) {
        this.config = config;
        client = MongoClients.create(connectionStringBuilder());
    }

    public static ConnectionManager getInstance(FileConfiguration config) {
        synchronized (ConnectionManager.class) {
            if (uniqueInstance == null) {
                uniqueInstance = new ConnectionManager(config);
            }
        }
        return uniqueInstance;
    }

    protected String connectionStringBuilder() {
        String configHost = config.getString("host", "127.0.0.1");
        int configPort = config.getInt("port", 27017);
        String configDB = config.getString("db", "mydb");
        boolean configAuth = config.getBoolean("auth", false);
        String configUser = config.getString("user", "user");
        String configPass = config.getString("pass", "pass");

        String connectionURL = "mongodb://";
        if (configAuth)
            connectionURL += configUser + ":" + String.valueOf(configPass) + "@";

        connectionURL += configHost + ":" + configPort + "/" + configDB;
        return connectionURL;
    }

    public void disconnect() {
        client.close();
        uniqueInstance = null;
    }

    public MongoClient getClient() {
        return client;
    }
}
