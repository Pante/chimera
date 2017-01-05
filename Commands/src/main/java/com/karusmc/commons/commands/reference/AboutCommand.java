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


public class AboutCommand extends PluginCommand {
    
    private String information;
    
    
    public AboutCommand(String name, Plugin plugin) {
        this(name, plugin, Criteria.PERMITTED);
    }
    
    public AboutCommand(String name, Plugin plugin, Criteria criteria) {
        super(name, plugin,  criteria);
        PluginDescriptionFile pluginDescription = plugin.getDescription();
        information = ChatColor.translateAlternateColorCodes('&', 
                "&6" + pluginDescription.getName() 
                + " version: &c" + pluginDescription.getVersion() 
                + "\n&6" + pluginDescription.getDescription()
                + "\nAuthor(s): &c" + pluginDescription.getAuthors().toString() 
                + "\n&6Source code & development resources: &c" + pluginDescription.getWebsite());
    }
    
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (criteria.test(this, sender, args)) {
            sender.sendMessage(information);
            
        } else {
            sender.sendMessage(ChatColor.RED + getPermissionMessage());
        }
    }
    
}
