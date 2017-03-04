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
package com.karuslabs.commons.commands.reference;

import com.karuslabs.commons.commands.*;
import com.karuslabs.commons.commands.events.CommandRegistrationEvent;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.*;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.commands.Utility.*;


/**
 * Represents a command which lists the plugin's command information.
 * May listen for {@link com.karuslabs.commons.commands.events.CommandRegistrationEvent} to
 * update itself. Command structure is flatten when stored to optimize look-up performance.
 */
public class HelpCommand extends PluginCommand implements Listener {
    
    private Map<String, Command> commands;
    private int size;
    
    
    /**
     * Creates a new command with the name and owning plugin specified.
     * 
     * @param name the name of the command
     * @param plugin the owning plugin of the command
     */
    public HelpCommand(String name, Plugin plugin) {
        this(name, plugin, (command, sender, args) -> Criteria.PERMITTED.test(command, sender, args) && Criteria.within(args, 0, 2), new HashMap<>(), 3);
    }
    
    /**
     * Creates a new command with the name, owning plugin, criteria, initial commands and page size specified.
     * 
     * @param name the name of the command
     * @param plugin the owning plugin of the command
     * @param criteria the criteria which must be satisfied for execution to proceed
     * @param commands the initial, flatten commands
     * @param size the size of a page
     */
    public HelpCommand(String name, Plugin plugin, Criteria criteria, Map<String, Command> commands, int size) {
        super(name, plugin, criteria);
        this.commands = commands;
        this.size = size;
    }
    
    
    /**
     * Displays the information of the commands if the criteria is satisfied; else displays the permission message. 
     * 
     * @param sender source object which is executing the command
     * @param args all arguments passed to the command, split via ' '
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (criteria.test(this, sender, args)) {
            int page = toInt(getArgumentOrDefault(args, 0, ""));
            String search = getArgumentOrDefault(args, 1, "");
            
            displayUsages(sender, page, search, getUsages(sender, page, search));
            
        } else {
            sender.sendMessage(ChatColor.RED + "You either do not have permission or an invalid number of arguments were specified.");
        }
    }
    
    /**
     * Returns an array of command usages based on the sender, page and search criteria specified.
     * 
     * @param sender source object which is executing the command
     * @param page the page of command usages
     * @param search the search criteria which a command's name must start with
     * @return an array of command usages
     */
    protected String[] getUsages(CommandSender sender, int page, String search) {
        List<String> usages = commands.values().stream()
                .filter(command -> command.getName().startsWith(search) && command.testPermissionSilent(sender))
                .limit(page * size)
                .map(command -> command.getUsage())
                .collect(Collectors.toList());
        
        return usages
                .subList(getFirstIndex(usages.size(), size, page), usages.size())
                .toArray(new String[0]);
    }
    
    
    private void displayUsages(CommandSender sender, int page, String search, String[] usages) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                "&6==== Help: &c" + search
                + " &6=== Page: &c" + page + "/" + " &6===="));
        sender.sendMessage(usages);
    }
    
    
    /**
     * Registers the command contained in the <code>event</code> specified to the list of known commands.
     * 
     * @see com.karuslabs.commons.commands.CommandMapProxy
     * 
     * @param event the command event
     */
    @EventHandler
    public void onRegister(CommandRegistrationEvent event) {
        Command command = event.getCommand();
        Utility.flapMap(command.getName(), command, commands);
    }
    
    
    /** 
     * @return a flatten map of commands
     */
    public Map<String, Command> getCommands() {
        return commands;
    }
    
}
