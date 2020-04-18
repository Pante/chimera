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
package com.karuslabs.commons.command.aot.lints;

import com.karuslabs.commons.command.aot.annotations.Command;
import com.karuslabs.commons.command.aot.lexers.Lexer;
import com.karuslabs.commons.command.aot.Node;

import java.util.*;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.*;


import static javax.tools.Diagnostic.Kind.ERROR;

import org.checkerframework.checker.nullness.qual.Nullable;


public class CommandLint extends Lint {

    Node current;
    @Nullable Set<Node> scope;
    
    
    public CommandLint(Lexer lexer, Map<Element, Set<Node>> scopes, Node root, Messager messager, Elements elements, Types types) {
        super(lexer, scopes, root, messager, elements, types);
        this.current = root;
    }

    
    @Override
    public void lint(Element element) {
        this.element = element;
        this.scope = scope(element);
        for (var command : element.getAnnotation(Command.class).value()) {
            lexer.lex(this, command, command);
            current = root;
        }
    }
    
    
    @Override
    public void argument(String context, String argument) {
        if (current == root) {
            messager.printMessage(
                ERROR, 
                "Invalid argument position: '<" + argument + ">' in '" + context + "', commands must start with literals", 
                element
            );
            return;
        }
        
        current = current.addChild(Node.argument(argument, element));
        scope.add(current);
    }
    
    
    @Override
    public void literal(String context, String literal) {
        current = current.addChild(Node.literal(literal, element));
        scope.add(current);
    }
    
    
    @Override
    public void alias(String context, String literal, String alias) {
        if (current.aliases.putIfAbsent(alias, element) != null) {
            var site = current.aliases.get(alias);
            error("Invalid alias: '"  + alias + "' in " + context + ", alias has already been declared in '" + site.asType().toString() + "'");
        }
    }

}
