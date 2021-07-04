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
package com.karuslabs.typist.generation.chunks;

import com.karuslabs.utilitary.Source;

import java.time.LocalDateTime;

import static com.karuslabs.typist.generation.chunks.Constants.VERSION;

/**
 * A generator that emits a header for a source file.
 */
public record Header() {
    
    /**
     * Generates a header for a source file.
     * 
     * @param source the resultant source
     * @param pack the package, or empty if the default package
     */
    public void emit(Source source, String pack) {
        if (!pack.isEmpty()) {
            source.line("package ", pack, ";").line();
        }
        
        source.append("""
               // CHECKSTYLE:OFF
                      
               import com.karuslabs.commons.command.Execution;
               import com.karuslabs.commons.command.tree.nodes.*;

               import com.mojang.brigadier.Command;
               import com.mojang.brigadier.suggestion.SuggestionProvider;
               import com.mojang.brigadier.tree.CommandNode;

               import java.util.*;
               import java.util.function.Predicate;

               import org.bukkit.command.CommandSender;

               /**
                * This file was generated at %s using Chimera %s
                */
               """.formatted(LocalDateTime.now(), VERSION));
    }

}
