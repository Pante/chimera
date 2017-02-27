/*
 * Copyright (C) 2016 KarusMC @ karusmc.com
 * All rights reserved.
 */
package com.karuslabs.commons.database;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class ConnectionManagerTest {
    @Mock
    private FileConfiguration fileConfiguration;

    private ConnectionManager connectionManager;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    @Parameters
    public void testConnectionStringBuilder(String host, int port, String db, boolean auth, String user, String pass,
                                            String values) throws Exception {
        when(fileConfiguration.getString("host", "127.0.0.1")).thenReturn(host);
        when(fileConfiguration.getInt("port", 27017)).thenReturn(port);
        when(fileConfiguration.getString("db", "mydb")).thenReturn(db);
        when(fileConfiguration.getBoolean("auth", false)).thenReturn(auth);
        when(fileConfiguration.getString("user", "user")).thenReturn(user);
        when(fileConfiguration.getString("pass", "pass")).thenReturn(pass);

        connectionManager = ConnectionManager.getInstance(fileConfiguration);

        assertEquals(values, connectionManager.connectionStringBuilder());
    }

    @After
    public void afterTestConnectionStringBuilder() {
        connectionManager.disconnect();
    }

    private Object[] parametersForTestConnectionStringBuilder() {
        return new Object[]{
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
                new Object[]{
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
