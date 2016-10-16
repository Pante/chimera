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
    
    private String pluginDescription;
    
    
    public AboutCommand(Plugin plugin, String name) {
        super(plugin, name);
        
        PluginDescriptionFile pluginDescription = plugin.getDescription();
        this.pluginDescription = ChatColor.translateAlternateColorCodes('&', 
                "&6" + pluginDescription.getName() 
                + "version: &c" + pluginDescription.getVersion() 
                + "\n&6" + pluginDescription.getDescription()
                + "\nAuthor(s): &c" + pluginDescription.getAuthors().toString() 
                + "\n&6Source code & development resources: &c" + pluginDescription.getWebsite());
    }
    
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (canUse(this, sender) && hasLength(0, args.length, 0)) {
            sender.sendMessage(pluginDescription);
            
        } else {
            sender.sendMessage(ChatColor.RED + getPermissionMessage());
        }
        
        return true;
    }
    
    
    protected String getPluginDescription() {
        return description;
    }
    
}
