/*
 * Copyright (C) 2016 KarusMC @ karusmc.com
 * All rights reserved.
 */
package com.karuslabs.commons.database;

import junitparams.*;

import org.junit.*;

import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


@RunWith(JUnitParamsRunner.class)
public class MongoConfigurationTest {

    private MongoConfiguration config;
    
    
    public MongoConfigurationTest() {
        config = new MongoConfiguration();
    }


    @Test
    @Parameters
    public void getURL(String host, int port, String name, boolean auth, String user, String pass, String url) {
        config.setHost(host).setPort(port).setName(name).setAuthentication(auth).setUser(user).setPassword(pass);

        assertEquals(url, config.getURL());
    }

    protected Object[] parametersForGetURL() {
        return new Object[] {
            // With authentication
            new Object[]{
                "192.168.1.1",
                12345,
                "database",
                true,
                "dark",
                "ajsdnaidb",
                "mongodb://dark:ajsdnaidb@192.168.1.1:12345/database"
            },
            // Without authentication
            new Object[] {
                "172.16.9.16",
                54321,
                "exampledb",
                false,
                "darks",
                "ajsdnaidb",
                "mongodb://172.16.9.16:54321/exampledb"
            }
        };
    }

}
