/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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
package com.karuslabs.typist.example.plugin;

import com.karuslabs.commons.command.OptionalContext;
import com.karuslabs.commons.command.types.PlayersType;

import com.karuslabs.typist.annotations.Bind;
import com.karuslabs.typist.annotations.Command;
import com.karuslabs.typist.annotations.Let;
import com.karuslabs.typist.annotations.Pack;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// Denotes the package of the generated command creator class. By default, it will
// always be in the same package as the class annotated with @Pack. 
@Pack(age = "com.karuslabs.example.plugin.commands") 
@Command({"typist:tell|typist:t <players> <message>"}) // We prefixed the command to avoid a name clash with vanilla Minecraft's tell command
public class TellCommand { 

    public static final @Bind(pattern = {"<message>"}) ArgumentType<String> message = StringArgumentType.string();
    public static final @Bind(pattern = {"<message>"}) SuggestionProvider<CommandSender> suggestions = (context, builder) -> builder.suggest("\"Hello World!\"").buildFuture();
    public final @Bind(pattern = {"<players>"}) PlayersType players = new PlayersType();
    
    // Binds a Command<CommandSender> to tell
    @Bind("typist:tell") 
    public static int tell(CommandContext<CommandSender> context) {
        context.getSource().sendMessage("Hello darkness my old friend");
        return 1;
    }
    
    
    // Binds a Execution<CommandSender> to <messages>
    @Bind(pattern = {"<message>"})
    public void send(CommandSender source, OptionalContext<CommandSender> context, @Let("<players>") List<Player> recipents, @Let String message) {
        recipents.forEach(p -> p.sendMessage(source.getName() + " says: " + message));
    }
    
}