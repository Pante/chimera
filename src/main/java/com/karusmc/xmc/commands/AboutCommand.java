/*
 * Copyright (C) 2016 PanteLegacy @ karusmc.com
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
package com.karusmc.xmc.commands;

import com.karusmc.xmc.core.*;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.*;

import static com.karusmc.xmc.util.Validator.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class AboutCommand extends XMCommand {
    
    private String information;
    
    
    public AboutCommand(Plugin plugin, String name) {
        super(plugin, name);
        information = getInformation(plugin.getDescription());
    }
    
    
    private String getInformation(PluginDescriptionFile description) {
        return ChatColor.translateAlternateColorCodes('&', 
                "&6" + description.getName() 
                + "version: &c" + description.getVersion() 
                + "\n&6" + description.getDescription()
                + "\nAuthor(s): &c" + description.getAuthors().toString() 
                + "\n&6Source code & development resources: &c" + description.getWebsite());
    }
    
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (canUse(this, sender) && args.length == 0) {
            sender.sendMessage(information);
            
        } else {
            sender.sendMessage(ChatColor.RED + getPermissionMessage());
        }
    }
  
}
