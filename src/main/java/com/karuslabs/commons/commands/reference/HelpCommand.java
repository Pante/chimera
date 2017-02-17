/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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

import com.karuslabs.commons.commands.Criteria;
import com.karuslabs.commons.commands.Command;
import com.karuslabs.commons.commands.Utility;
import com.karuslabs.commons.commands.PluginCommand;
import com.karuslabs.commons.commands.events.CommandRegistrationEvent;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.*;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.commands.Utility.*;


/**
 * Represents a command which retrieves and displays the owning plug-in's commands and their usages.
 * Listens for {@link com.karusmc.commons.commands.events.CommandRegistrationEvent} and automatically
 * updates itself. Command hierarchy is flatten when stored to optimize look-up performance.
 */
public class HelpCommand extends PluginCommand implements Listener {
    
    private Map<String, Command> commands;
    private int size;
    
    
    /**
     * Constructs this with the specified name and owning plug-in.
     * 
     * @param name The name of this command
     * @param plugin The owning plug-in of this command
     */
    public HelpCommand(String name, Plugin plugin) {
        this(name, plugin, (command, sender, args) -> Criteria.PERMITTED.test(command, sender, args) && Criteria.within(args, 0, 2), new HashMap<>(), 3);
    }
    
    /**
     * Constructs this with the specified name, owning plug-in, execution criteria, commands and page size.
     * 
     * @param name The name of this command
     * @param plugin The owning plug-in of this command
     * @param criteria The execution criteria
     * @param commands The map containing flatten hierarchy of commands
     * @param size The number of commands on each page page
     */
    public HelpCommand(String name, Plugin plugin, Criteria criteria, Map<String, Command> commands, int size) {
        super(name, plugin, criteria);
        this.commands = commands;
        this.size = size;
    }
    
    
    /**
     * Tests the criteria and displays the processed command information.
     * 
     * @param sender Source object which is executing this command
     * @param args All arguments passed to the command, split via ' '
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
     * Called when a command is registered to a {@link com.karusmc.commons.commands.CommandMapProxy}.
     * 
     * @param event The event called
     */
    @EventHandler
    public void onRegister(CommandRegistrationEvent event) {
        Command command = event.getCommand();
        Utility.flapMap(command.getName(), command, commands);
    }
    
    
    /** 
     * @return A flatten representation of commands.
     */
    public Map<String, Command> getCommands() {
        return commands;
    }
    
}
