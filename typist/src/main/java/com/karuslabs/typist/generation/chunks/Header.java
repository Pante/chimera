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

import com.karuslabs.elementary.processor.Source;

import java.time.LocalDateTime;

public class Header {

    private static final String VERSION = "4.9.0-SNAPSHOT";
    
    public void emit(Source source, String pack) {
        if (!pack.isEmpty()) {
            source.line("package ", pack, ";");
        }
        
        source.line("import com.karuslabs.commons.command.Execution;")
              .line("import com.karuslabs.commons.command.tree.nodes.*;")
              .line()
              .line("import com.mojang.brigadier.Command;")
              .line("import com.mojang.brigadier.suggestion.SuggestionProvider;")
              .line("import com.mojang.brigadier.tree.CommandNode;")
              .line()
              .line("import java.util.*;")
              .line("import java.util.function.Predicate;")
              .line()
              .line("import org.bukkit.command.CommandSender;")
              .line()
              .line("/**")
              .line(" * This file was generated at ", LocalDateTime.now(), " using Chimera ", VERSION)
              .line(" */");
    }

}
