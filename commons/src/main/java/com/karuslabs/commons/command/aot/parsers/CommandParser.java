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
package com.karuslabs.commons.command.aot.parsers;

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.lexers.Lexer;
import com.karuslabs.Satisfactory.*;
import java.lang.annotation.Annotation;

import java.util.*;
import javax.lang.model.element.*;

public class CommandParser extends LexParser {

    public CommandParser(Logger logger, Lexer lexer) {
        super(logger, lexer);
    }

    @Override
    public void parse(Environment environment, Element element) {
        var namespace = environment.namespace(element);
        var lines = element.getAnnotation(com.karuslabs.commons.command.aot.annotations.Command.class).value();
        if (lines.length == 0) {
            logger.error(element, "@Command annotation should not be empty");
            return;
        }
        
        for (var line : lines) {
            Command parent = null;
            var commands = namespace;
            
            for (var token : lexer.lex(logger, element, line)) {
                var command = commands.get(token.identity);
                if (command == null) {
                    command = new Command(parent, token.identity, (TypeElement) element);
                    commands.put(token.identity, command);
                }
                
                merge(element, command, token);
                
                parent = command;
                commands = command.children;
            }
        }
    }
    
    void merge(Element element, Command command, Token token) {
        if (token.aliases.length == 0) {
            return;
        }
        
        var duplicates = new HashSet<>();
        for (var alias : token.aliases) {
            if (!command.aliases.add(alias)) {
                duplicates.add(alias);
            }
        }
        
        if (!duplicates.isEmpty()) {
            logger.warn(element, token.lexeme, "contains duplicate aliases: " + Texts.and(duplicates, (value, builder) -> builder.append('"').append(value).append('"')));
        }
        
        if (command.aliases.contains(command.identity.name)) {
            logger.warn(element, token.lexeme, "contains an alias that is the same as its name");
        }
    }
    
    @Override
    public Class<? extends Annotation> annotation() {
        return com.karuslabs.commons.command.aot.annotations.Command.class;
    }

}
