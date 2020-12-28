/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.commons.command.aot.generation.chunks;

import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.tree.CommandNode;

import java.util.*;
import java.util.function.Predicate;

import org.bukkit.command.CommandSender;


/**
 * This file was generated at 2020-06-03T13:06:03.378589600 using Chimera 4.7.0
 */
public class Commands {

    private static final Predicate<CommandSender> REQUIREMENT = s -> true;

    public static Map<String, CommandNode<CommandSender>> of(com.karusmc.raster.TellCommand source) {
        var commands = new HashMap<String, CommandNode<CommandSender>>();

        var command = new Argument<>("message", com.karusmc.raster.TellCommand.message, source::send, REQUIREMENT, com.karusmc.raster.TellCommand.suggestions);

        var command0 = new Argument<>("players", source.players, null, REQUIREMENT, null);
        command0.addChild(command);

        var command1 = new Literal<>("tell", (context, source) -> {
            return com.karusmc.raster.TellCommand.tell(context, source, context.getArgument("a", Source.class), context.getArgument("b", A.class));
            }, REQUIREMENT
        );
        Literal.alias(command1, "t");
        command1.addChild(command0);

        commands.put(command1.getName(), command1);
        return commands;
    }

}
