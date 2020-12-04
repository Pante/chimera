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
package com.karuslabs.commons.command.aot.old;

import com.karuslabs.smoke.Logger;
import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.annotations.Command;
import com.karuslabs.commons.command.aot.old.Lexer;

import java.util.*;
import javax.lang.model.element.*;

import static com.karuslabs.smoke.Messages.quote;

public class CommandParser extends TokenParser {

    public CommandParser(Logger logger, Lexer lexer) {
        super(logger, lexer);
    }

    @Override
    protected void process(Element element, Map<Identity, Mirrors.Command> namespace) {
        var lines = element.getAnnotation(Command.class).value();
        if (lines.length == 0) {
            logger.error("@Command annotation should not be empty");
            return;
        }
        
        for (var line : lines) {
            var commands = namespace;
            
            var tokens = lexer.lex(logger, line);
            for (var token : tokens) {
                var command = commands.get(token.identifier);
                if (command == null) {
                    command = new Mirrors.Command(token.identifier, (TypeElement) element, token.aliases);
                    commands.put(token.identifier, command);
                    
                } else if (!token.aliases.isEmpty()) {
                    mergeAliases(command, token);
                }
                
                commands = command.children;
            }
        }
    }
    
    void mergeAliases(Mirrors.Command command, Token token) {
        var intersection = new HashSet<>(command.aliases);
        intersection.retainAll(token.aliases);
        if (!intersection.isEmpty()) {
            logger.warn(quote(command.identifier.lexeme) + " contains duplicate aliases: " + quote(String.join(", ", intersection)));
        }

        command.aliases.addAll(token.aliases);
    }

}
