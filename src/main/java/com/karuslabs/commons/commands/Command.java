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
package com.karuslabs.commons.commands;

import java.util.*;

import org.bukkit.command.CommandSender;


public abstract class Command extends org.bukkit.command.Command {

    protected Criteria criteria;
    
    
    public Command(String name, Criteria criteria) {
        this(name, criteria, "", "", new ArrayList<>());
    }
    
    public Command(String name, Criteria criteria, String description, String message, List<String> aliases) {
        super(name, description, message, aliases);
        this.criteria = criteria;
    }
    
    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        execute(sender, args);
        return true;
    }
    
    
    public abstract void execute(CommandSender sender, String[] args);
    
    
    public Criteria getCriteria() {
        return criteria;
    }
    
    
    public Command newAliases(List<String> aliases) {
        setAliases(aliases);
        return this;
    }
    
    public Command newDescription(String description) {
        setDescription(description);
        return this;
    }
    
    public Command newLabel(String label) {
        setLabel(label);
        return this;
    }
    
    public Command newName(String name) {
        setName(name);
        return this;
    }
    
    public Command newPermission(String permission) {
        setPermission(permission);
        return this;
    }
    
    public Command newPermissionMessage(String message) {
        setPermissionMessage(message);
        return this;
    }
    
    public Command newUsage(String usage) {
        setUsage(usage);
        return this;
    }
    
}
