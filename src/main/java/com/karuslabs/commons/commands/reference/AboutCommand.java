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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.*;


/**
 * Represents a <code>Command</code> which returns the information defined in the plugin.yml file of the owning plug-in.
 */
public class AboutCommand extends PluginCommand {
    
    private String information;
    
    
    /**
     * Creates a new command with the name and owning plug-in specified.
     * 
     * @param name the name of the command
     * @param plugin the owning plug-in of the command
     */
    public AboutCommand(String name, Plugin plugin) {
        this(name, plugin, Criteria.PERMITTED);
    }
    
    /**
     * Creates a new command with the name, owning plug-in and criteria specified.
     * 
     * @param name the name of the command
     * @param plugin the owning plug-in of the command
     * @param criteria the criteria which must be satisfied for execution to proceed
     */
    public AboutCommand(String name, Plugin plugin, Criteria criteria) {
        super(name, plugin,  criteria);
        PluginDescriptionFile description = plugin.getDescription();
        
        information = ChatColor.translateAlternateColorCodes('&', 
                "&6" + description.getName() + " version: &c" + description.getVersion() 
                + "\n&6" + description.getDescription()
                + "\nAuthor(s): &c" + description.getAuthors().toString() 
                + "\n&6Source code & development resources: &c" + description.getWebsite());
    }
    
    
    /**
     * Displays the plug-in information to the sender if the criteria is satisfied; else displays the permission message
     * 
     * @param sender source object which is executing the command
     * @param args all arguments passed to the command, split via ' '
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (criteria.test(this, sender, args)) {
            sender.sendMessage(information);
            
        } else {
            sender.sendMessage(ChatColor.RED + getPermissionMessage());
        }
    }
    
}
