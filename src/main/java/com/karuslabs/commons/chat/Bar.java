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
package com.karuslabs.commons.chat;

import org.bukkit.entity.Player;


public interface Bar {
    
    public static final Bar INSTANCE = new Bar() {
        @Override
        public void sendMessage(Player player, String message) {
            player.sendMessage(message);
        }

        @Override
        public void sendMessage(Player player, String message, int fadeIn, int stay, int fadeOut) {
            player.sendMessage(message);
        }
        
    };
    
    
    public void sendMessage(Player player, String message);
    
    public void sendMessage(Player player, String message, int fadeIn, int stay, int fadeOut);
            
}
