/*
 * Copyright (C) 2016 KarusMC @ karusmc.com
 * All rights reserved.
 */
package com.karuslabs.commons.database;

public class DatabaseConfiguration {

    private String host = "127.0.0.1";
    private int port = 27017;
    private String db = "mydb";
    private boolean auth = false;
    private String user = "user";
    private String pass = "pass";

    public DatabaseConfiguration() {
    }

    public DatabaseConfiguration(String host, int port, String db) {
        this.host = host;
        this.port = port;
        this.db = db;
    }

    public DatabaseConfiguration(String host, int port, String db, String user, String pass) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.auth = true;
        this.user = user;
        this.pass = pass;
    }

    public String getHost() {
        return host;
    }

    public DatabaseConfiguration setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public DatabaseConfiguration setPort(int port) {
        this.port = port;
        return this;
    }

    public String getDb() {
        return db;
    }

    public DatabaseConfiguration setDb(String db) {
        this.db = db;
        return this;
    }

    protected boolean isAuth() {
        return auth;
    }

    public DatabaseConfiguration setAuth(boolean auth) {
        this.auth = auth;
        return this;
    }

    protected String getUser() {
        return user;
    }

    public DatabaseConfiguration setUser(String user) {
        this.user = user;
        this.auth = true;
        return this;
    }

    protected String getPass() {
        return pass;
    }

    public DatabaseConfiguration setPass(String pass) {
        this.pass = pass;
        this.auth = true;
        return this;
    }

}
