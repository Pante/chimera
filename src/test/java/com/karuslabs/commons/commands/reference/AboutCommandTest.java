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

import com.google.common.collect.Lists;

import com.karuslabs.commons.commands.Criteria;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.*;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class AboutCommandTest {
    
    private Plugin plugin;
    private PluginDescriptionFile file;
    private CommandSender sender;
    
    
    public AboutCommandTest() {
        plugin = mock(Plugin.class);
        file = mock(PluginDescriptionFile.class);  
        sender = mock(CommandSender.class);
        
        when(plugin.getDescription()).thenReturn(file);
    }
    
    
    @Test
    public void execute() {
        when(file.getName()).thenReturn("name");
        when(file.getVersion()).thenReturn("does it matter?");
        when(file.getDescription()).thenReturn("Going to be rewritten sooner or latter");
        when(file.getAuthors()).thenReturn(Lists.newArrayList("Pante"));
        when(file.getWebsite()).thenReturn("www.karuslabs.com/sell-out");
        
        new AboutCommand("name", plugin, Criteria.NONE).execute(sender, null);
        
        verify(sender, times(1)).sendMessage(ChatColor.translateAlternateColorCodes('&', 
                "&6name version: &cdoes it matter?"
                + "\n&6Going to be rewritten sooner or latter"
                + "\nAuthor(s): &c[Pante]"
                + "\n&6Source code & development resources: &cwww.karuslabs.com/sell-out"));
    }
    
}
