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
package com.karuslabs.commons.util.database;

import org.bukkit.configuration.ConfigurationSection;


public class Databases {
    
    public static String getMongoURL(ConfigurationSection section) {
        String url = "mongodb://";
        if (section.getBoolean("authentication", false)) {
            url += section.getString("host", "") + ":" + section.getString("password", "") + "@";
        } 
        url += section.getString("host", "") + ":" + section.getInt("port", 27018) + "/" + section.getString("name", "");
        
        return url;
    }
    
}
