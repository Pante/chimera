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


/**
 * Represents a Command, which executes various tasks upon user input.
 */
public abstract class Command extends org.bukkit.command.Command {
    
    protected Criteria criteria;
    
    
    /**
     * Constructs this with the specified name and and execution criteria.
     * 
     * @param name The name of this command
     * @param criteria The execution criteria
     */
    public Command(String name, Criteria criteria) {
        this(name, criteria, "", "", new ArrayList<>());
    }
    
    /**
     * Constructs that with the specified name, execution criteria, description, permission message, and aliases.
     * 
     * @param name The name of this command
     * @param criteria The execution criteria
     * @param description The description of this command
     * @param message The permission message of this command
     * @param aliases The aliases of this command
     */
    public Command(String name, Criteria criteria, String description, String message, List<String> aliases) {
        super(name, description, message, aliases);
        this.criteria = criteria;
    }
    
    
    /**
     * Wrapper method that delegates execution to {@link #execute(org.bukkit.command.CommandSender, java.lang.String[])}
     * 
     * @param sender Source object which is executing this command
     * @param label The alias of the command used
     * @param args All arguments passed to the command, split via ' '
     * @return always returns true
     */
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        execute(sender, args);
        return true;
    }
    
    
    /**
     * Executes the command.
     * 
     * @param sender Source object which is executing this command
     * @param args All arguments passed to the command, split via ' '
     */
    public abstract void execute(CommandSender sender, String[] args);
    
    
    /**
     * @return The execution criteria for this command
     */
    public Criteria getCriteria() {
        return criteria;
    }
    
}
