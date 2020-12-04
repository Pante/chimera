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

import com.karuslabs.smoke.Filter;
import com.karuslabs.smoke.Logger;
import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.old.Mirrors.*;
import com.karuslabs.commons.command.aot.annotations.Bind;
import com.karuslabs.commons.command.aot.old.Lexer;

import java.util.*;
import javax.lang.model.element.Element;

import static java.util.stream.Collectors.toList;

public class BindParser extends TokenParser {
    
    private final Binder binder;
    
    public BindParser(Binder binder, Logger logger, Lexer lexer) {
        super(logger, lexer);
        this.binder = binder;
    }
    
    @Override
    protected void process(Element element, Map<Identity, Command> namespace) {
        if (namespace.isEmpty()) {
            logger.zone(element.accept(Filter.CLASS, null)).error("Class should be annotated with @Command");
            return;
        }
        
        var bindings = element.getAnnotation(Bind.class).value();
        if (bindings.length == 0) {
            logger.error("@Bind annotation should not be empty");
            return;
        }
        
        for (var binding : bindings) {
            var tokens = lexer.lex(logger, binding);
            if (tokens.isEmpty()) {
                continue;
            }
            
            var member = element.accept(binder, tokens.get(tokens.size() - 1).identifier);
            if (member == null) {
                continue;
            }
            
            if (tokens.size() == 1 && !match(member, namespace)) {
                logger.error(member.identifier, "does not exist");
                
            } else if (tokens.size() > 1) {
                find(tokens, member, namespace);
            }
        }
    }
    
    boolean match(Member member, Map<Identity, Command> namespace) {
        var match = false;
        for (var entry : namespace.entrySet()) {
            match(member, entry.getValue().children);
            if (member.identifier.equals(entry.getKey())) {
                entry.getValue().members.put(member.site, member);
                match = true;
            }
        }
        
        return match;
    }
    
    void find(List<Token> tokens, Member member, Map<Identity, Command> namespace) {
        Command command = null;
        for (var token : tokens) {
            command = namespace.get(token.identity);
            
            if (command == null) {
                logger.error(String.join(" ", tokens.stream().map(t -> t.identity.toString()).collect(toList())), " does not exist");
                return;
            }
            
            namespace = command.children;
        }
        
        command.members.put(member.site, member);
    }
    
}