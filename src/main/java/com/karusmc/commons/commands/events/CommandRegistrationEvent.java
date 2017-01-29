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
package com.karusmc.commons.commands.events;

import com.karusmc.commons.commands.Command;

import org.bukkit.event.*;


/**
 * Called when a command is registered to a {@link com.karusmc.commons.commands.CommandMapProxy}
 */
public class CommandRegistrationEvent extends Event implements Cancellable {

    private static HandlerList handlers = new HandlerList();
    
    
    private Command command;
    private boolean cancelled;

    
    /**
     * Constructs this with the specified command.
     * 
     * @param command The command that was registered
     */
    public CommandRegistrationEvent(Command command) {
        this.command = command;
        cancelled = false;
    }
    
    
    /**
     * @return The command that was registered
     */
    public Command getCommand() {
        return command;
    }
    
    
    /**
     * @return The HandlerList
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    
    /**
     * @return true if the event is cancelled and false otherwise
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether the event is cancelled.
     * 
     * @param cancelled true if the event is cancelled and false otherwise
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
}
