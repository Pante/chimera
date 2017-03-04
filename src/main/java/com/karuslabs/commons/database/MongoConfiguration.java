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


/**
 * Represents a configuration for MongoDB.
 */
public class MongoConfiguration {

    private String host;
    private int port;
    private String name;
    private boolean authentication;
    private String user;
    private String password;

    
    /**
     * Creates a new configuration.
     */
    public MongoConfiguration() {
        this("127.0.0.1", 27017, "mydb");
    }
    
    /**
     * Creates a new configuration with the host, port and name specified.
     * 
     * @param host the host
     * @param port the port
     * @param name the name
     */
    public MongoConfiguration(String host, int port, String name) {
        this(host, port, name, false, "user", "pass");
    }
    
    /**
     * Creates a new configuration with authentication and the host, port, name, user and password specified.
     * 
     * @param host the host
     * @param port the port
     * @param name the name
     * @param user the user
     * @param password the password
     */
    public MongoConfiguration(String host, int port, String name, String user, String password) {
        this(host, port, name, true, user, password);
    }

    private MongoConfiguration(String host, int port, String name, boolean authentication, String user, String password) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.authentication = authentication;
        this.user = user;
        this.password = password;
    }

    
    public String getHost() {
        return host;
    }

    public MongoConfiguration setHost(String host) {
        this.host = host;
        return this;
    }

    
    public int getPort() {
        return port;
    }

    public MongoConfiguration setPort(int port) {
        this.port = port;
        return this;
    }

    
    public String getName() {
        return name;
    }

    public MongoConfiguration setName(String name) {
        this.name = name;
        return this;
    }

    
    protected boolean needsAuthentication() {
        return authentication;
    }

    public MongoConfiguration setAuthentication(boolean authentication) {
        this.authentication = authentication;
        return this;
    }

    
    public String getUser() {
        return user;
    }

    public MongoConfiguration setUser(String user) {
        this.user = user;
        return this;
    }

    
    public String getPassword() {
        return password;
    }

    public MongoConfiguration setPassword(String password) {
        this.password = password;
        return this;
    }
    
    
    public String getURL() {
        String url = "mongodb://";
        if (authentication) {
            url += user + ":" + password + "@";
        } 
        url += host + ":" + port + "/" + name;
        
        return url;
    }

}
