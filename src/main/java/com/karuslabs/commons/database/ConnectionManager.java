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

import com.mongodb.async.client.*;


public class ConnectionManager {

    private MongoClient client;

    
    public ConnectionManager(String url) {
        client = MongoClients.create(url);
    }

    public ConnectionManager(DatabaseConfiguration dc) {
        client = MongoClients.create(connectionStringBuilder(dc));
    }

    
    protected String connectionStringBuilder(DatabaseConfiguration dc) {
        String configHost = dc.getHost();
        int configPort = dc.getPort();
        String configDB = dc.getDb();
        boolean configAuth = dc.isAuth();
        String configUser = dc.getUser();
        String configPass = dc.getPass();

        String connectionURL = "mongodb://";
        if (configAuth)
            connectionURL += configUser + ":" + String.valueOf(configPass) + "@";

        connectionURL += configHost + ":" + configPort + "/" + configDB;
        return connectionURL;
    }

    
    public void disconnect() {
        client.close();
    }

    public MongoClient getClient() {
        return client;
    }
}
