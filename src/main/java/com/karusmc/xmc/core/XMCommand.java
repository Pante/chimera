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

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public abstract class XMCommand extends Command implements PluginIdentifiableCommand {
    
    private Plugin owningPlugin;
    
    private boolean consoleAllowed;
    private boolean defaultAllowed;
    
    
    public XMCommand(Plugin owningPlugin, String name) {
        super(name);
        this.owningPlugin = owningPlugin;
    }
    
    
    @Override
    public Plugin getPlugin() {
        return owningPlugin;
    }
    
    
    public boolean isConsoleAllowed() {
        return consoleAllowed;
    }
    
    public void setConsoleAllowed(boolean consoleAllowed) {
        this.consoleAllowed = consoleAllowed;
    }
    
    
    public boolean isDefaultAllowed() {
        return defaultAllowed;
    }
    
    public void setDefaultAllowed(boolean defaultAllowed) {
        this.defaultAllowed = defaultAllowed;
    }
    
}
