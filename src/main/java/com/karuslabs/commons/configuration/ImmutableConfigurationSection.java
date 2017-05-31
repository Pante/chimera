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
package com.karuslabs.commons.configuration;

import java.util.*;

import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


public class ImmutableConfigurationSection implements ConfigurationSection {    
    
    private ConfigurationSection config;

    
    public ImmutableConfigurationSection(ConfigurationSection config) {
        this.config = config;
    }


    @Override
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    @Override
    public Map<String, Object> getValues(boolean deep) {
        return config.getValues(deep);
    }

    @Override
    public boolean contains(String path) {
        return config.contains(path);
    }

    @Override
    public boolean contains(String path, boolean ignoreDefault) {
        return config.contains(path, ignoreDefault);
    }

    @Override
    public boolean isSet(String path) {
        return config.isSet(path);
    }

    @Override
    public String getCurrentPath() {
        return config.getCurrentPath();
    }

    @Override
    public String getName() {
        return config.getName();
    }

    @Override
    public Configuration getRoot() {
        return config.getRoot();
    }

    @Override
    public ConfigurationSection getParent() {
        ConfigurationSection parent = config.getParent();
        if (parent != null) {
            return new ImmutableConfigurationSection(parent);
            
        } else {
            return null;
        }
    }

    @Override
    public Object get(String path) {
        Object object = config.get(path);
        if (object instanceof ConfigurationSection) {
            return new ImmutableConfigurationSection((ConfigurationSection) object);
            
        } else {
            return object;
        }
    }

    @Override
    public Object get(String path, Object def) {
        Object object = config.get(path, def);
        if (object instanceof ConfigurationSection) {
            return new ImmutableConfigurationSection((ConfigurationSection) object);
            
        } else {
            return object;
        }
    }

    @Override
    public void set(String path, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConfigurationSection createSection(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConfigurationSection createSection(String path, Map<?, ?> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getString(String path) {
        return config.getString(path);
    }
    
    @Override
    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    @Override
    public boolean isString(String path) {
        return config.isString(path);
    }

    @Override
    public int getInt(String path) {
        return config.getInt(path);
    }

    @Override
    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    @Override
    public boolean isInt(String path) {
        return config.isInt(path);
    }

    @Override
    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    @Override
    public boolean isBoolean(String path) {
        return config.isBoolean(path);
    }

    @Override
    public double getDouble(String path) {
        return config.getDouble(path);
    }

    @Override
    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }

    @Override
    public boolean isDouble(String path) {
        return config.isDouble(path);
    }

    @Override
    public long getLong(String path) {
        return config.getLong(path);
    }

    @Override
    public long getLong(String path, long def) {
        return config.getLong(path, def);
    }

    @Override
    public boolean isLong(String path) {
        return config.isLong(path);
    }

    @Override
    public List<?> getList(String path) {
        return config.getList(path);
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        return config.getList(path, def);
    }

    @Override
    public boolean isList(String path) {
        return config.isList(path);
    }

    @Override
    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        return config.getIntegerList(path);
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        return config.getBooleanList(path);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return config.getDoubleList(path);
    }

    @Override
    public List<Float> getFloatList(String path) {
        return config.getFloatList(path);
    }

    @Override
    public List<Long> getLongList(String path) {
        return config.getLongList(path);
    }

    @Override
    public List<Byte> getByteList(String path) {
        return config.getByteList(path);
    }
    
    @Override
    public List<Character> getCharacterList(String path) {
        return config.getCharacterList(path);
    }

    @Override
    public List<Short> getShortList(String path) {
        return config.getShortList(path);
    }

    @Override
    public List<Map<?, ?>> getMapList(String path) {
        return config.getMapList(path);
    }

    @Override
    public Vector getVector(String path) {
        return config.getVector(path);
    }

    @Override
    public Vector getVector(String path, Vector def) {
        return config.getVector(path, def);
    }

    @Override
    public boolean isVector(String path) {
        return config.isVector(path);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String path) {
        return config.getOfflinePlayer(path);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        return config.getOfflinePlayer(path, def);
    }

    @Override
    public boolean isOfflinePlayer(String path) {
        return config.isOfflinePlayer(path);
    }

    @Override
    public ItemStack getItemStack(String path) {
        return config.getItemStack(path);
    }

    @Override
    public ItemStack getItemStack(String path, ItemStack def) {
        return config.getItemStack(path, def);
    }

    @Override
    public boolean isItemStack(String path) {
        return config.isItemStack(path);
    }

    @Override
    public Color getColor(String path) {
        return config.getColor(path);
    }

    @Override
    public Color getColor(String path, Color def) {
        return config.getColor(path, def);
    }

    @Override
    public boolean isColor(String path) {
        return config.isColor(path);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path) {
        return config.getConfigurationSection(path);
    }

    @Override
    public boolean isConfigurationSection(String path) {
        return config.isConfigurationSection(path);
    }

    @Override
    public ConfigurationSection getDefaultSection() {
        return config.getDefaultSection();
    }

    @Override
    public void addDefault(String path, Object value) {
        config.addDefault(path, value);
    }

}
