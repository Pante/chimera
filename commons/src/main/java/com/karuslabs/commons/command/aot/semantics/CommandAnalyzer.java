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
package com.karuslabs.commons.command.aot.semantics;

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.annotations.Command;
import com.karuslabs.commons.command.aot.lexers.Lexer;
import com.karuslabs.commons.command.aot.ir.*;

import java.util.List;
import javax.lang.model.element.Element;


public class CommandAnalyzer extends Analyzer {

    private Lexer lexer;
    
    
    public CommandAnalyzer(Environment environment, Lexer lexer) {
        super(environment);
        this.lexer = lexer;
    }
    
    
    @Override
    public void analyze(Element element) {
        environment.initialize(element);
        var scope = environment.scope(element);
        
        var commands = element.getAnnotation(Command.class).value();
        if (commands.length == 0) {
            environment.error("Invalid @Command annotation, @Command cannot be empty");
            return;
        }
        
        for (var command : commands) {
            parse(scope, element, lexer.lex(environment, command, command));
        }
    }
    
    void parse(IR root, Element element, List<Token> tokens) {
        var current = root;
        for (var token : tokens) {
            var ir = current.add(environment, element, token);
            if (ir == null) {
                return;
            }
            
            current = ir;
        }
    }

}
