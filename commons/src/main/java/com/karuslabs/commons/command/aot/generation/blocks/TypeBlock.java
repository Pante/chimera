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
package com.karuslabs.commons.command.aot.generation.blocks;

import java.time.LocalDateTime;



public class TypeBlock {

    StringBuilder builder;
    
    
    public TypeBlock() {
        builder = new StringBuilder();
    }
    
    
    public void start(String pack, String type) {
        builder.setLength(0);
        builder.append("// This file was generated at ").append(LocalDateTime.now()).append(" using Chimera AOT 4.6.0\n");
        if (!pack.isEmpty()) {
            builder.append("package ").append(pack).append(";\n\n");
        }
        builder.append("import com.karuslabs.commons.command.tree.nodes.*;\n\n");
        builder.append("import com.mojang.brigadier.tree.CommandNode;\n\n");
        builder.append("import java.util.*;\n");
        builder.append("import java.util.function.Predicate;\n\n");
        builder.append("import org.bukkit.command.CommandSender;\n\n\n");
        builder.append("public class ").append(type).append(" {\n\n");
        builder.append("    private static final Predicate<CommandSender> REQUIREMENT = s -> true;\n\n");
    }
    
    
    public void method(String method) {
        builder.append(method);
    }
    
    
    public String end() {
        return builder.append("}\n").toString();
    }
    
}
