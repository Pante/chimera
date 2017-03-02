/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
