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
package com.karusmc.xmc.integration.xml;

import com.karusmc.xmc.core.ConfigurableCommand;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class StubCommand extends ConfigurableCommand {

    public StubCommand(Plugin owningPlugin, String name) {
        super(owningPlugin, name);
    }

    
    @Override
    public void execute(CommandSender sender, String[] args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
