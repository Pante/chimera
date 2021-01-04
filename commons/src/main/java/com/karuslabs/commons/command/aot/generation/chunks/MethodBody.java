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

import com.karuslabs.commons.command.aot.Command;
import com.karuslabs.puff.generation.Source;

import java.util.*;
import javax.lang.model.element.TypeElement;

public class MethodBody {
    
    private final CommandInstantiation instantiation;
    
    public MethodBody(CommandInstantiation instantiation) {
        this.instantiation = instantiation;
    }
    
    public void emit(Source source, TypeElement site, Collection<Command> commands) {
        source.line("public static Map<String, CommandNode<CommandSender>> of(", site.getQualifiedName(), " source) {")
              .indent()
                .line("var commands = new HashMap<String, CommandNode<CommandSender>>();");
                 for (var command : commands) {
                     var name = visit(source, command);
                     source.line("commands.put(", name, ".getName(), ", name, ");");
                 }
          source.line("return commands;")
              .unindent()
              .line("}");
    }
    
    String visit(Source source, Command command) {
        var children = new ArrayList<String>();
        for (var child : command.children.values()) {
            children.add(visit(source, child));
            source.line();
        }
        
        var name = instantiation.emit(source, command);
        for (var child : children) {
            source.line(name, ".addChild(", child, ");");
        }
        
        return name;
    }
    
}
