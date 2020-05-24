/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.command.tree.nodes;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;

import java.util.stream.Stream;

import org.bukkit.command.CommandSender;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;


class MutableTest {
    
    
    @ParameterizedTest
    @MethodSource("mutables")
    void removeChild(Mutable<CommandSender> mutable) {
        var child = Literal.of("child").alias("child1").build();
        
        mutable.addChild(child);
        mutable.removeChild("child");
        
        assertEquals(2, ((CommandNode<?>) mutable).getChildren().size());
        if (mutable instanceof Aliasable<?>) {
            assertEquals(2, ((Aliasable<CommandSender>) mutable).aliases().get(0).getChildren().size());
        }
    }
    
    
    @ParameterizedTest
    @MethodSource("mutables")
    void setCommand(Mutable<CommandSender> mutable) {
        Command command = val -> 0;
        
        mutable.setCommand(command);
        
        assertEquals(command, ((CommandNode<?>) mutable).getCommand());
        if (mutable instanceof Aliasable<?>) {
            assertEquals(command, ((Aliasable<CommandSender>) mutable).aliases().get(0).getCommand());
        }
    }
    
    
    @ParameterizedTest
    @MethodSource("mutables")
    void setRedirect(Mutable<CommandSender> mutable) {
        var destination = Literal.of("child").build();
        
        mutable.setRedirect(destination);
        
        assertEquals(destination, ((CommandNode<?>) mutable).getRedirect());
        if (mutable instanceof Aliasable<?>) {
            assertEquals(destination, ((Aliasable<CommandSender>) mutable).aliases().get(0).getRedirect());
        }
    }
    
    
    @ParameterizedTest
    @MethodSource("mutables")
    void optionally(Mutable<CommandSender> mutable) {
        var command = (CommandNode<?>) mutable;

        assertEquals(2, command.getChildren().size());
        assertNotNull(command.getChild("optional"));
        assertNotNull(command.getChild("optional child"));
    }
    
    
    static Stream<Mutable<CommandSender>> mutables() {
        var optional = Literal.of("optional").then(Literal.of("optional child"));
        return Stream.of(
            Literal.of("literal").alias("literal alias").alias("a", "b").executes((s, val) -> {val.getSource().getName();}).optionally(optional).build(),
            Argument.of("argument", StringArgumentType.word()).executes((s, val) -> {val.getSource().getName();}).optionally(optional).build()
        );
    }

} 
