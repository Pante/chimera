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
package com.karusmc.xmc.util;

import com.karusmc.xmc.core.*;

import java.util.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class Commands {
    
    public static void flatMap(XMCommand command, Map<String, XMCommand> commands) {
        flapMap(command.getName(), command, commands);
    }
    
    private static void flapMap(String name, XMCommand command, Map<String, XMCommand> commands) {
        if (command instanceof Dispatcher) {
            ((Dispatcher) command).getCommands().values().forEach(subcommand -> flapMap(name + " " + subcommand.getName(), subcommand, commands));
        }
        
        commands.put(name, command);
    }
    
    
    public static<T> T getArgumentOrDefault(T[] args, int index, T defaultArgument) {
        if (args.length > index) {
            return args[index];
    
        } else {
            return defaultArgument;
        }
    }
    
    public static String[] trimArguments(String[] args) {
        if (args.length <= 1) {
            return new String[]{};

        } else {
            return Arrays.copyOfRange(args, 1, args.length - 1);
        }
    }

}
