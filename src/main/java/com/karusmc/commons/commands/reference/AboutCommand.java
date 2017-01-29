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
package com.karusmc.commons.commands.reference;

import com.karusmc.commons.commands.*;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.*;


/**
 * Represents a command which formats and returns information as defined in the owning plug-ins
 * plugin.yml file.
 */
public class AboutCommand extends PluginCommand {
    
    private String information;
    
    
    /**
     * Constructs this with the specified name and owning plug-in
     * 
     * @param name The name of this command
     * @param plugin The owning plug-in of this command
     */
    public AboutCommand(String name, Plugin plugin) {
        this(name, plugin, Criteria.PERMITTED);
    }
    
    /**
     * Constructs this with the specified name, owning plug-in and execution criteria.
     * 
     * @param name The name of this command
     * @param plugin The owning plug-in of this command
     * @param criteria The execution criteria
     */
    public AboutCommand(String name, Plugin plugin, Criteria criteria) {
        super(name, plugin,  criteria);
        PluginDescriptionFile description = plugin.getDescription();
        
        information = ChatColor.translateAlternateColorCodes('&', 
                "&6" + description.getName() 
                + " version: &c" + description.getVersion() 
                + "\n&6" + description.getDescription()
                + "\nAuthor(s): &c" + description.getAuthors().toString() 
                + "\n&6Source code & development resources: &c" + description.getWebsite());
    }
    
    
    /**
     * Tests the criteria and displays the owning plug-in's information to the sender.
     * 
     * @param sender Source object which is executing this command
     * @param args All arguments passed to the command, split via ' '
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
