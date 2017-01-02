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
package com.karusmc.commons.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;


@FunctionalInterface
public interface Criteria {
    
    public static final Criteria NONE = (command, sender, args) -> true;
    public static final Criteria PERMITTED = (command, sender, args) -> command.testPermissionSilent(sender);
    public static final Criteria PERMITTEDPLAYER = (command, sender, args) -> sender instanceof Player && command.testPermissionSilent(sender);
    
    
    public boolean test(Command command, CommandSender sender, String[] args);
    
    
    public static boolean hasLength(int min, int length, int max) {
        return min <= length && length <= max;
    }
    
}
