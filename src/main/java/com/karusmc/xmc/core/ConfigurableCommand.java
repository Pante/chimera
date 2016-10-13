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
package com.karusmc.xmc.core;

import java.util.*;

import org.bukkit.plugin.Plugin;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public abstract class ConfigurableCommand extends XMCommand {
    
    private long cooldown;
    
    private boolean blacklist;
    private Set<String> worlds;
    
    
    public ConfigurableCommand(Plugin owningPlugin, String name) {
        super(owningPlugin, name);
        cooldown = 0;
        
        blacklist = false;
        worlds = new HashSet<>();
    }
    
    
    public long getCooldown() {
        return cooldown;
    }
    
    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }
    
    
    public boolean hasBlacklist() {
        return blacklist;
    }
    
    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }
    
    
    public Set<String> getWorlds() {
        return worlds;
    }
    
    public void setWorlds(Set<String> worlds) {
        this.worlds = worlds;
    }
    
}
